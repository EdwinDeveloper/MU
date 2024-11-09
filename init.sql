DO
$$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'crud_database') THEN
      CREATE DATABASE crud_database;
   END IF;
END
$$;