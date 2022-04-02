CREATE TABLE t_user (
  `id` int NOT NULL,
  `username` varchar(64) NOT NULL,
  `password` char(32) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO t_user (id, username, password) VALUES (123, 'username', 'e9ceeb95824c23a5ea00a7f64b22e324');