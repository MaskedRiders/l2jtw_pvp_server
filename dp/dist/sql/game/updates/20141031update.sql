ALTER TABLE `character_customs`
DROP COLUMN `battle_score_best`,
ADD COLUMN `battle_score_best`  int(10) NULL DEFAULT NULL COMMENT '戦闘スコアベスト' AFTER `battle_score`,
ADD COLUMN `pvp_death_date`  bigint(20) NULL COMMENT 'PvP死亡時刻' AFTER `tvt_score_log`,
ADD COLUMN `pvp_zombie`  tinyint(1) NULL COMMENT 'ゾンビ' AFTER `pvp_death_date`;

MODIFY COLUMN `battle_score_best_date`  bigint(20) NULL DEFAULT NULL COMMENT '戦闘スコアベスト時刻' AFTER `battle_score_best`;
