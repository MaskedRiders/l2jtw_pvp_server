-- ----------------------------
-- Table structure for chatdata
-- ----------------------------
DROP TABLE IF EXISTS `chatdata`;
CREATE TABLE IF NOT EXISTS `chatdata` (
  `npc_id` int(11) NOT NULL,
  `chat_type` int(11) NOT NULL,
  `chat_delay` int(11) NOT NULL,
  `chat_chance` int(11) NOT NULL,
  `chat_condition` int(11) NOT NULL,
  `chat_value1` int(11) NOT NULL,
  `chat_condition_end` int(11) NOT NULL,
  `chat_value2` int(11) NOT NULL DEFAULT '0',
  `chat_memo` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;