SHOW PROCESSLIST;
SELECT * FROM `module` WHERE module_id IN(99,1,100,2,3,9,8,76) ORDER BY module_channel, module_father_id,action_order;
UPDATE `role_module` SET access  = '3'  