# db-pg-multitenant-poc

This repository is a playground/experiment for setting up a multitenant Spring Boot app with
PostgreSQL Row Level Security (RLS).
It demonstrates a simple server storing documents per tenant, as well as public documents available
for all tenants, where each user may only retrieve documents of those tenants that are assigned to
them.

The server creates a token that contains a tenants claim when a user signs in and that the client
should send with every request.
Every time a database connection is retrieved, it will automatically set the tenant context based
on the claims of the currently authenticated user.
Combined with RLS, this allows to offload tenant isolation from the business logic.

## Requirements

- [Docker Compose](https://docs.docker.com/compose/install)
- Kotlin Compiler (for example, bundled with [IntelliJ IDEA](https://www.jetbrains.com/idea/))
- A database client (for example, [DBeaver](https://dbeaver.io))

## Setup

### Environment

Create a copy of `.env.template` and name it `.env`:

```shell
cp .env.template .env
```

The secret values will be empty – fill those in.
You can change other values if you wish.

### Initialization

Start the database with the following command:

```shell
docker compose up db
```

With any database client (IDEA, DBeaver), connect to the database using your superuser credentials (`POSTGRES_USER` and `POSTGRES_PASSWORD`) at:

```
jdbc:postgresql://localhost:5432/mtdb
```

if your `POSTGRES_DB` is not `mtdb`, change accordingly.

### Creating application users

Next, create database users that the Spring Boot app will use to connect, substituting the `<placeholders>` to the respective values in your `.env` file:

```sql
CREATE USER <APP_POSTGRES_USERNAME> ENCRYPTED PASSWORD '<APP_POSTGRES_PASSWORD>';
GRANT app TO <APP_POSTGRES_USERNAME>;

CREATE USER <APP_AUTH_POSTGRES_USERNAME> ENCRYPTED PASSWORD '<APP_POSTGRES_PASSWORD>';
ALTER USER <APP_AUTH_POSTGRES_USERNAME> BYPASSRLS;
GRANT auth TO <APP_AUTH_POSTGRES_USERNAME>;
```

The first user (with the `app` role) is the main user the application will connect with.
It has wide permissions but has RLS checks enabled, only seeing entries for specified tenants at a time.

The second user (with the `auth` role) is another user the application will use to fetch registered users and tenants they are allowed to access to perform authentication.
It can bypass RLS checks, but only has limited access to a few tables.

### Creating a JAR

Before you can build a Docker image for the server, you need to assemble a JAR archive:

```shell
./gradlew bootJar
```

### Finishing setup

At this point, you can add additional connections to the database in your database client if you wish, for example with the application user and specific tenants.

To assign tenants to a connection, add the following in the respective startup script/bootstrap query in your database client:

```sql
SET app.tenant = '{1}';
```

This will associate tenant with ID `1` to the connection.
You can also specify several tenants in the array, for example `{1,2}`.

> [!CAUTION]
> The database data is stored in the `db` folder.
> Should you remove it, you will have to repeat these steps again.

## Run

```shell
docker compose up
```

This will start the server and database if any of them are not running yet.
When you're done, shut both down with:

```shell
docker compose down
```

### Creating users

You can use any preferred method to send requests to the server, which is available at
`http://localhost:8081` (or other port if you changed `SB_PORT`).
Check out example requests in [/http](/http/auth.http).

By sending a request to `/api/signup` and following with `/api/signin`, you will obtain a token,
somewhat like this:

```
eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJoZWxsb0B3b3JsZC5jb20iLCJzdWItaWQiOjE3LCJ0ZW4taWQiOlsxLDJdLCJpYXQiOjE3NzE3MTg3MjMsImV4cCI6MTc3MTcyMjMyM30.yXgnQHyfFyH3nAFsrtq4wiOtKXf5KiFybVh45XghAWj_dBMEEO-SJnC8wirpj7br
```

You can paste it on [jwt.io](https://jwt.io) to check out its contents.
It should contain several claims such as `sub` (username), `sub-id` (user ID), and `ten-id`
(tenant IDs).

### Sending requests

Other endpoints require authentication with the obtained token by sending the following header:

```
Authorization: Bearer <token>
```

Check out [/http](/http/app.http) for examples.
When the server receives a token and the authentication is successful, it will store the claims
in the request's thread.
Then, every time a database connection is obtained from the pool, it will use the `ten-id` claim
to tell the database what tenants the current user should have access to.