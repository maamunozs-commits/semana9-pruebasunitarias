-- Script general Oracle para ambos microservicios.
-- No contiene credenciales reales. Configure su usuario en Oracle Cloud y use:
-- DB_URL=DB_URL_AQUI
-- DB_USERNAME=DB_USERNAME_AQUI
-- DB_PASSWORD=DB_PASSWORD_AQUI
--
-- Si necesita crear un usuario, hagalo con sus propios datos:
-- CREATE USER DB_USERNAME_AQUI IDENTIFIED BY DB_PASSWORD_AQUI;
-- GRANT CONNECT, RESOURCE, UNLIMITED TABLESPACE TO DB_USERNAME_AQUI;
--
-- Luego ejecute, conectado como ese usuario:
-- @database/pedidos.sql
-- @database/citas.sql

@@database/pedidos.sql
@@database/citas.sql
