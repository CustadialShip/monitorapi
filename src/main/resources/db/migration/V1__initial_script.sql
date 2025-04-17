--
-- Table structure for table `types`
--

CREATE TABLE IF NOT EXISTS `types` (
    `name` varchar(36) NOT NULL,
    PRIMARY KEY (`name`),
    UNIQUE KEY `id_UNIQUE` (`name`)
);

--
-- Table structure for table `units`
--

CREATE TABLE IF NOT EXISTS `units` (
    `name` varchar(36) NOT NULL,
    PRIMARY KEY (`name`),
    UNIQUE KEY `id_UNIQUE` (`name`)
);

--
-- Table structure for table `sensors`
--

CREATE TABLE IF NOT EXISTS `sensors` (
    `id` varchar(36) NOT NULL,
    `name` varchar(30) NOT NULL,
    `model` varchar(15) NOT NULL,
    `range_from` int DEFAULT NULL,
    `range_to` int NOT NULL,
    `type` varchar(36) NOT NULL,
    `unit` varchar(36) DEFAULT NULL,
    `location` varchar(40) DEFAULT NULL,
    `description` varchar(200) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`),
    KEY `fk_sensors_type_idx` (`type`),
    KEY `fk_sensors_unit_idx` (`unit`),
    CONSTRAINT `fk_sensors_type` FOREIGN KEY (`type`) REFERENCES `types` (`name`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_sensors_unit` FOREIGN KEY (`unit`) REFERENCES `units` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
);

