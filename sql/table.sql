CREATE TABLE t_user (
  `id` int NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` char(32) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO t_user (id, username, password) VALUES (123, 'username', 'e9ceeb95824c23a5ea00a7f64b22e324');




create table t_job(
id int not null primary key auto_increment,
job_name varchar(64) not null ,
description varchar(255) default '' comment '任务的功能描述',
cron varchar(32) not null ,
function_name varchar(64) not null,
type tinyint default 0,
status tinyint default 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;;

INSERT INTO t_job (id, job_name, description, cron, function_name, type, status) VALUES (1, 'SayHello', 'print hello every minute', '0 * * * * ? ', 'sayHello', 1, 1);
INSERT INTO t_job (id, job_name, description, cron, function_name, type, status) VALUES (2, 'GenerateAndPrintId', 'get an id from snowflake every 5 seconds', '0/5 * * * * ? ', 'generateAndPrintId', 1, 1);