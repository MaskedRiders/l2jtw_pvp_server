-- ----------------------------
-- Table structure for npcCharData
-- ----------------------------
DROP TABLE IF EXISTS `npcCharData`;
CREATE TABLE IF NOT EXISTS `npcCharData` (
  `npc_id` int(11) NOT NULL DEFAULT '0',
  `ischar` int(11) DEFAULT NULL,
  `charclass` int(11) DEFAULT NULL,
  `charrace` int(11) DEFAULT NULL,
  `charface` int(11) DEFAULT NULL,
  `charhair` int(11) DEFAULT NULL,
  `charhaircolor` int(11) DEFAULT NULL,
  `charcolor` varchar(11) DEFAULT NULL,
  `charhero` int(11) DEFAULT NULL,
  `charsex` int(11) DEFAULT NULL,
  `lrhand` decimal(11,0) DEFAULT NULL,
  `enchlvl` decimal(11,0) DEFAULT NULL,
  `armor` decimal(11,0) DEFAULT NULL,
  `pant` decimal(11,0) DEFAULT NULL,
  `head` decimal(11,0) DEFAULT NULL,
  `boot` decimal(11,0) DEFAULT NULL,
  `glove` decimal(11,0) DEFAULT NULL,
  `back` decimal(11,0) DEFAULT NULL,
  `hair` decimal(11,0) DEFAULT NULL,
  `dhair` decimal(11,0) DEFAULT NULL,
  `face` decimal(11,0) DEFAULT NULL,
  `augmentation` decimal(11,0) DEFAULT NULL,
  PRIMARY KEY (`npc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;