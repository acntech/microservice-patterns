CREATE TABLESPACE microservicepatterns LOCATION '/var/lib/postgresql/data';

CREATE USER customers WITH PASSWORD '3Ky9cToKN9CQ';
CREATE USER ordering WITH PASSWORD '7f15MUKYFRtb';
CREATE USER warehouse WITH PASSWORD 'sV57umzIk2hV';
CREATE USER shipping WITH PASSWORD 'lBq6eNrt092Q';
CREATE USER billing WITH PASSWORD 'W5Zn9kfyeZwm';

CREATE DATABASE customers WITH OWNER customers TABLESPACE microservicepatterns;
CREATE DATABASE ordering WITH OWNER ordering TABLESPACE microservicepatterns;
CREATE DATABASE warehouse WITH OWNER warehouse TABLESPACE microservicepatterns;
CREATE DATABASE shipping WITH OWNER shipping TABLESPACE microservicepatterns;
CREATE DATABASE billing WITH OWNER billing TABLESPACE microservicepatterns;
