# db-pg-multitenant-poc

This repository is a playground/experiment for setting up a multitenant Spring Boot app with PostgreSQL Row Level Security (RLS).

## Requirements

[Docker Compose](https://docs.docker.com/compose/install).

## Setup

### Environment

Create a copy of `.env.template` and name it `.env`:

```sh
cp .env.template .env
```

The password values will be empty – fill those in.
You can change other values if you wish.

### Initialization

Start the database with the following command:

```sh
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
GRANT auth TO <APP_AUTH_POSTGRES_USERNAME>;
```

The first user (with the `app` role) is the main user the application will connect with.
It has wide permissions but has RLS checks enabled, only seeing entries for specified tenants at a time.

The second user (with the `auth` role) is another user the application will use to fetch registered users and tenants they are allowed to access to perform authentication.
It can bypass RLS checks, but only has limited access to a few tables.

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

Finally, shut down the database using:

```sh
docker compose down db
```
