-- This can be used to check for views without security_invoker.
SELECT
	pgv.schemaname,
	pgv.viewname,
	pgv.viewowner,
	pgc.reloptions,
	pgv.definition
FROM pg_catalog.pg_views pgv
JOIN pg_catalog.pg_class pgc ON pgc.relname = pgv.viewname AND pgc.relkind = 'v'
WHERE
	pgv.schemaname = 'public'
	AND (
		pgc.reloptions IS NULL
		OR NOT ('security_invoker=true' = ANY(pgc.reloptions)
	)
);
