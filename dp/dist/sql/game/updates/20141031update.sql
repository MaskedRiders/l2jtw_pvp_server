ALTER TABLE `character_customs`
MODIFY COLUMN `battle_score_best_date`  int(10) NULL DEFAULT NULL COMMENT '戦闘スコアベスト時刻' AFTER `battle_score_best`;
