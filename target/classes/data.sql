--
-- 設定台灣時區(+08:00)
--

SET @@session.time_zone = '+08:00';
select NOW();



--
-- establish database `badminton_club` 建立資料庫
--

DROP DATABASE  IF EXISTS `badminton_club`;

CREATE DATABASE  IF NOT EXISTS `badminton_club`;
USE `badminton_club`;



--
-- Table structure for table `member` 會員
--

DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `mem_user`    varchar(10) NOT NULL COMMENT '會員帳號',
  `mem_name`     varchar(10) NOT NULL COMMENT '會員姓名',
  `mem_birth`  date NOT NULL COMMENT '會員生日' ,
  `mem_gen`  varchar(1) NOT NULL COMMENT '會員性別' ,
  `mem_idn`  varchar(11) NOT NULL COMMENT '會員身分證' ,
  `mem_phone`  varchar(11) NOT NULL COMMENT '會員手機號碼' ,
  `mem_mail`  varchar(30) COMMENT '會員電子信箱' ,
  `mem_addr`  varchar(70) NOT NULL COMMENT '會員地址' ,
  `mem_img`  varchar(30) COMMENT '會員圖片' ,
  `mem_info`  tinyint(1) NOT NULL COMMENT '信箱通知' ,
  `mem_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '註冊時間' ,
  PRIMARY KEY (mem_user)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='會員';

INSERT INTO `member` 
(`mem_user`,`mem_name`,`mem_birth`,`mem_gen`,`mem_idn`,`mem_phone`,`mem_mail`,`mem_addr`,`mem_img`,`mem_info`) VALUES 
('john95','陳約翰','1990-07-04','M','A229884653','0923887465','john95450@gmail.com','新北市新莊市建國一路46號','/images/member/john95.jpeg',0);

INSERT INTO `member` 
(`mem_user`,`mem_name`,`mem_birth`,`mem_gen`,`mem_idn`,`mem_phone`,`mem_mail`,`mem_addr`,`mem_img`,`mem_info`) VALUES 
('mary12','林瑪麗','1994-05-20','W','H773556398','0998654345','mary665@gmail.com','台北市大安區羅斯福路四段3號','/images/member/mary12.jpeg',0);

INSERT INTO `member` 
(`mem_user`,`mem_name`,`mem_birth`,`mem_gen`,`mem_idn`,`mem_phone`,`mem_mail`,`mem_addr`,`mem_img`,`mem_info`) VALUES 
('tom887','王湯尼','1986-11-14','M','C884663563','0977445321','susan0922@gmail.com','基隆市暖暖區源遠路22號','/images/member/tom887.jpeg',0);

INSERT INTO `member` 
(`mem_user`,`mem_name`,`mem_birth`,`mem_gen`,`mem_idn`,`mem_phone`,`mem_mail`,`mem_addr`,`mem_img`,`mem_info`) VALUES 
('winnie','徐溫尼','1987-08-01','W','H773009465','0987788987','winnie223@gmail.com','新北市三峽區中正路二段400號','/images/member/winnie.jpeg',0);



--
-- Table structure for table `users` 會員帳密
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL COMMENT '會員帳號',
  `password` char(68) NOT NULL COMMENT '會員密碼',
  `enabled` tinyint(1) NOT NULL COMMENT '會員資格',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='會員帳密';


INSERT INTO `users` 
VALUES 
('john95','{bcrypt}$2a$10$PuTq6g4QEQ4te9yYibh5SeP6pJOCWqKLits30JPGab4hdjgzv6Lz2',1),
('mary12','{bcrypt}$2a$10$PuTq6g4QEQ4te9yYibh5SeP6pJOCWqKLits30JPGab4hdjgzv6Lz2',1),
('tom887','{bcrypt}$2a$10$PuTq6g4QEQ4te9yYibh5SeP6pJOCWqKLits30JPGab4hdjgzv6Lz2',1),
('winnie','{bcrypt}$2a$10$PuTq6g4QEQ4te9yYibh5SeP6pJOCWqKLits30JPGab4hdjgzv6Lz2',1);



--
-- Table structure for table `roles` 角色 
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `authority` varchar(50) NOT NULL COMMENT '角色權限編號',
  `role_name`  varchar(20) NOT NULL COMMENT '角色名稱',
  PRIMARY KEY (authority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色';


INSERT INTO `roles`(`authority`,`role_name`) VALUES ('ROLE_MEMBER','會員');
INSERT INTO `roles`(`authority`,`role_name`) VALUES ('ROLE_MANAGER','管理員');
INSERT INTO `roles`(`authority`,`role_name`) VALUES ('ROLE_SURPERMANAGER','超級管理員');




--
-- Table structure for table `authorities` 權限
--

DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `authority_no` int(10) NOT NULL AUTO_INCREMENT COMMENT '權限編號',
  `username` varchar(50) NOT NULL COMMENT '會員帳號',
  `authority` varchar(50) NOT NULL COMMENT '角色權限編號',
  PRIMARY KEY (authority_no),
  UNIQUE KEY `authorities_idx_1` (`username`,`authority`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`username`) REFERENCES `users` (`username`),
  CONSTRAINT `authorities_ibfk_2` FOREIGN KEY (`authority`) REFERENCES `roles` (`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='權限';


INSERT INTO `authorities`(`username`,`authority`) 
VALUES 
('john95','ROLE_MEMBER'),
('mary12','ROLE_MEMBER'),
('mary12','ROLE_MANAGER'),
('tom887','ROLE_MEMBER'),
('tom887','ROLE_MANAGER'),
('winnie','ROLE_MEMBER'),
('winnie','ROLE_MANAGER'),
('winnie','ROLE_SURPERMANAGER');



--
-- Table structure for table `systems` 系統設定
--

DROP TABLE IF EXISTS `systems`;
CREATE TABLE `systems` (
  `sys_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '系統設定編號',
  `sys_name`     varchar(25) NOT NULL COMMENT '系統設定名稱',
  `sys_data`  varchar(100) COMMENT '系統設定資料' ,
  `sys_con`  text COMMENT '系統設定內容' ,
  PRIMARY KEY (sys_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系統設定';

INSERT INTO `systems`(`sys_name`,`sys_data`,`sys_con`) VALUES ('系統信箱','hsuj1047@gmail.com','clan380clue746');
INSERT INTO `systems`(`sys_name`,`sys_con`) VALUES ('關於我們','歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社歡迎來到羽球社');
INSERT INTO `systems`(`sys_name`,`sys_con`) VALUES ('聯絡資訊','許嘉雯：0912768906王美美：0945674335許嘉雯：0912768906王美美：0945674335許嘉雯：0912768906');
INSERT INTO `systems`(`sys_name`,`sys_data`) VALUES ('QrCode','/images/qrCode.png');



--
-- Table structure for table `activity_type` 活動類型
--

DROP TABLE IF EXISTS `activity_type`;
CREATE TABLE `activity_type` (
  `avt_type_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '活動類型編號',
  `avt_type_name`     varchar(25) NOT NULL COMMENT '系活動類型名稱',
  `avt_type_pre` tinyint(1) NOT NULL COMMENT '活動須經審核' ,
  PRIMARY KEY (avt_type_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活動類型';

INSERT INTO `activity_type`(`avt_type_name`,`avt_type_pre`) VALUES ('羽球比賽',1);
INSERT INTO `activity_type`(`avt_type_name`,`avt_type_pre`) VALUES ('聚餐',0);
INSERT INTO `activity_type`(`avt_type_name`,`avt_type_pre`) VALUES ('社遊',1);
INSERT INTO `activity_type`(`avt_type_name`,`avt_type_pre`) VALUES ('其他',0);



--
-- Table structure for table `activity` 活動
--


DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `avt_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '活動編號',
  `avt_name`     varchar(30) NOT NULL COMMENT '活動名稱',
  `avt_date_s`  date NOT NULL COMMENT '活動日期(起)' ,
  `avt_date_e`  date NOT NULL COMMENT '活動日期(迄)' ,
  `avt_gat_date`  TIMESTAMP COMMENT '活動集合時間' ,
  `avt_loc`  varchar(40) COMMENT '集合地點' ,
  `avt_cut_date`  date COMMENT '活動截止日期' ,
  `avt_upp`  int(5) COMMENT '活動名額上限' ,
  `avt_low`  int(5) COMMENT '活動名額下限' ,
  `avt_price`  int(5) COMMENT '活動費用' ,
  `avt_intr`  text COMMENT '活動介紹' ,
  `avt_memo`  text COMMENT '活動備註' ,
  `avt_img`  varchar(30) COMMENT '活動圖片' ,
  `avt_type_no`  int(10) NOT NULL COMMENT '活動類型編號' ,
  `avt_stat`  varchar(15) COMMENT '活動狀態' ,
  `avt_pre`  tinyint(1) NOT NULL COMMENT '活動審核' ,
  `avt_edit`  tinyint(1) NOT NULL COMMENT '活動草稿' ,
  `avt_only`  tinyint(1) NOT NULL COMMENT '僅會員參加' ,
  `avt_frd_data`  tinyint(1) NOT NULL COMMENT '需親友資料' ,
  `mem_user`    varchar(10) NOT NULL COMMENT '會員編號',
  PRIMARY KEY (avt_no),
  CONSTRAINT `activity_ibfk_1` FOREIGN KEY (`avt_type_no`) REFERENCES `activity_type` (`avt_type_no`),
  CONSTRAINT `activity_ibfk_2` FOREIGN KEY (`mem_user`) REFERENCES `member` (`mem_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活動';

INSERT INTO `activity` 
(`avt_name`,`avt_date_s`,`avt_date_e`,`avt_gat_date`,`avt_loc`,`avt_cut_date`,`avt_upp`,`avt_low`,`avt_price`,`avt_intr`,`avt_memo`,`avt_img`,`avt_type_no`,`avt_stat`,`avt_pre`,`avt_edit`,`avt_only`,`avt_frd_data`,`mem_user`) VALUES 
('草嶺古道','2019-10-10','2019-10-10','2019-10-10 08:20:00','台北車站北一門','2019-10-03',20,3,20,'草嶺古道為東北角最具代表性的歷史人文山徑，是19世紀台灣北部與東北部、淡水廳與噶瑪蘭廳的官道路段。此段古遺址，以「雄鎮蠻煙」碣和「虎」字碑為最。除此之外，散布在古道上的人文遺跡，尚有跌死馬橋、土地公寺和客棧遺址。 草嶺古道在夏季時期野薑花花香漫山飄香、秋季期滿山五節芒花盛開白色花草隨風搖曳，埡口之處可遠望龜山島，是東北角海岸國家步道最受歡迎的健行路線。','請背小背包，穿著運動服裝，運動鞋，現金、健保卡、悠遊卡加值300、午餐、行動水、行動糧、雨衣、禦寒衣物、帽子、鹽巴等等','/images/activity/avt_1.jpg',3,'已截止',1,0,0,1,'mary12');

INSERT INTO `activity` 
(`avt_name`,`avt_date_s`,`avt_date_e`,`avt_gat_date`,`avt_loc`,`avt_cut_date`,`avt_upp`,`avt_low`,`avt_price`,`avt_intr`,`avt_memo`,`avt_img`,`avt_type_no`,`avt_stat`,`avt_pre`,`avt_edit`,`avt_only`,`avt_frd_data`,`mem_user`) VALUES 
('羽球比賽','2019-09-28','2019-09-28','2019-09-28 15:30:00','公司門口','2019-09-22',10,4,0,'公司羽球友誼賽','無','/images/activity/avt_2.jpg',1,'已結束',1,0,1,0,'tom887');

INSERT INTO `activity` 
(`avt_name`,`avt_date_s`,`avt_date_e`,`avt_gat_date`,`avt_loc`,`avt_cut_date`,`avt_upp`,`avt_low`,`avt_price`,`avt_intr`,`avt_memo`,`avt_img`,`avt_type_no`,`avt_stat`,`avt_pre`,`avt_edit`,`avt_only`,`avt_frd_data`,`mem_user`) VALUES 
('聖誕大餐','2019-12-24','2019-12-24','2019-12-24 19:00:00','公司門口','2019-12-20',10,2,350,'難得聖誕節大家一起聚聚吧','記得帶交換禮物','/images/activity/avt_3.png',2,'報名中',1,0,0,0,'winnie');

INSERT INTO `activity` 
(`avt_name`,`avt_date_s`,`avt_date_e`,`avt_gat_date`,`avt_loc`,`avt_cut_date`,`avt_upp`,`avt_low`,`avt_price`,`avt_intr`,`avt_memo`,`avt_img`,`avt_type_no`,`avt_stat`,`avt_pre`,`avt_edit`,`avt_only`,`avt_frd_data`,`mem_user`) VALUES 
('桌遊趣','2019-11-02','2019-11-02','2019-11-02 14:00:00','公館捷運站1號出口','2019-10-25',7,3,150,'繁忙的生活，我們來點輕鬆休閒交誼式的活動吧。char(10)當天桌遊種類：風聲、說書人、富饒之城、種豆、BANG、美麗島風雲、牛魔王 … 等。(種類包含說故事、策略、運氣、心機等等都有)','桌兔子網站 http://rabbitable.com/','/images/activity/avt_4.jpg',4,'已額滿',1,0,0,0,'tom887');

INSERT INTO `activity` 
(`avt_name`,`avt_date_s`,`avt_date_e`,`avt_gat_date`,`avt_loc`,`avt_cut_date`,`avt_upp`,`avt_low`,`avt_price`,`avt_intr`,`avt_memo`,`avt_img`,`avt_type_no`,`avt_stat`,`avt_pre`,`avt_edit`,`avt_only`,`avt_frd_data`,`mem_user`) VALUES 
('合歡山賞雪團','2020-02-01','2020-02-02','2020-02-01 08:00:00','台北車站東三門','2020-01-02',12,3,2500,'來去合歡山賞雪唷！！！第1天：台北車站東三門 ─ 埔里 ─ 清境 ─ 合歡山之旅 ─ 晚餐。第2天：早餐 ─ 清境農場之旅 ─ 埔里觀光酒廠 ─ 台北','2/1晚上住滑雪山莊，有提供晚餐和2/2早餐唷。','/images/activity/avt_5.jpeg',3,'報名中',1,0,0,1,'winnie');

INSERT INTO `activity` 
(`avt_name`,`avt_date_s`,`avt_date_e`,`avt_gat_date`,`avt_loc`,`avt_cut_date`,`avt_upp`,`avt_low`,`avt_price`,`avt_intr`,`avt_memo`,`avt_img`,`avt_type_no`,`avt_stat`,`avt_pre`,`avt_edit`,`avt_only`,`avt_frd_data`,`mem_user`) VALUES 
('羽球交友賽','2019-12-29','2019-12-29','2019-12-29 14:30:00','公司門口','2019-12-24',12,4,100,'讓我們一邊羽球一邊認識新朋友吧','無','/images/activity/avt_6.jpg',1,'報名中',0,0,1,0,'mary12');

INSERT INTO `activity` 
(`avt_name`,`avt_date_s`,`avt_date_e`,`avt_gat_date`,`avt_loc`,`avt_cut_date`,`avt_upp`,`avt_low`,`avt_price`,`avt_intr`,`avt_memo`,`avt_img`,`avt_type_no`,`avt_stat`,`avt_pre`,`avt_edit`,`avt_only`,`avt_frd_data`,`mem_user`) VALUES 
('跨年烤肉','2019-12-31','2019-12-31','2019-12-31 20:00:00','河濱公園','2019-12-27',8,3,350,'迎接2020年，一起來烤肉跨年吧！','天氣冷，注意保暖唷！','/images/activity/avt_7.jpg',4,'報名中',0,0,0,0,'tom887');

INSERT INTO `activity` 
(`avt_name`,`avt_date_s`,`avt_date_e`,`avt_gat_date`,`avt_loc`,`avt_cut_date`,`avt_upp`,`avt_low`,`avt_price`,`avt_intr`,`avt_memo`,`avt_img`,`avt_type_no`,`avt_pre`,`avt_edit`,`avt_only`,`avt_frd_data`,`mem_user`) VALUES 
('蛋糕DIY','2020-01-11','2020-01-11','2020-01-11 14:00:00','DIY教室','2020-01-04',10,3,200,'喜歡甜點的你們，一起來做蛋糕吧！','無','/images/activity/avt_8.jpg',4,0,1,0,0,'mary12');

--
-- Table structure for table `advocate` 活動宣傳圖片
--

DROP TABLE IF EXISTS `advocate`;
CREATE TABLE `advocate` (
  `adv_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '宣傳編號',
  `adv_img`     varchar(50) NOT NULL COMMENT '宣傳圖片',
  `avt_no`    int(10) NOT NULL COMMENT '活動編號',
  PRIMARY KEY (adv_no),
  CONSTRAINT `advocate_ibfk_1` FOREIGN KEY (`avt_no`) REFERENCES `activity` (`avt_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活動宣傳圖片';

INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_1/adv_1.jpg',1);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_1/adv_2.jpg',1);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_1/adv_3.jpeg',1);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_2/adv_1.jpg',2);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_2/adv_2.jpg',2);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_5/adv_1.jpg',5);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_5/adv_2.jpg',5);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_5/adv_3.jpg',5);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_5/adv_4.jpg',5);
INSERT INTO `advocate`(`adv_img`,`avt_no`) VALUES ('/images/activity/avt_5/adv_5.jpg',5);


--
-- Table structure for table `avt_message` 活動心得回饋
--

DROP TABLE IF EXISTS `avt_message`;
CREATE TABLE `avt_message` (
  `msg_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '回饋編號',
  `msg_con`     text NOT NULL COMMENT '回饋內容',
  `msg_star`    int(1) COMMENT '星號評分',
  `mem_user`    varchar(10) NOT NULL COMMENT '會員編號',
  `avt_no`    int(10) NOT NULL COMMENT '活動編號',
  `msg_time`    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回饋時間',
  PRIMARY KEY (msg_no),
  CONSTRAINT `avt_message_ibfk_1` FOREIGN KEY (`mem_user`) REFERENCES `member` (`mem_user`),
  CONSTRAINT `avt_message_ibfk_2` FOREIGN KEY (`avt_no`) REFERENCES `activity` (`avt_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活動心得回饋';

INSERT INTO `avt_message`(`msg_con`,`msg_star`,`mem_user`,`avt_no`) VALUES ('主辦辛苦囉',4,'john95',2);
INSERT INTO `avt_message`(`msg_con`,`msg_star`,`mem_user`,`avt_no`) VALUES ('活動很讚',5,'mary12',2);
INSERT INTO `avt_message`(`msg_con`,`msg_star`,`mem_user`,`avt_no`) VALUES ('很好玩',4,'tom887',2);
INSERT INTO `avt_message`(`msg_con`,`msg_star`,`mem_user`,`avt_no`) VALUES ('good',4,'winnie',2);
INSERT INTO `avt_message`(`msg_con`,`msg_star`,`mem_user`,`avt_no`) VALUES ('覺得很棒',4,'winnie',1);




--
-- Table structure for table `avt_preview` 活動審核
--

DROP TABLE IF EXISTS `avt_preview`;
CREATE TABLE `avt_preview` (
  `avt_pre_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '活動審核編號',
  `mem_user`    varchar(10) NOT NULL COMMENT '會員編號',
  `avt_no`    int(10) NOT NULL COMMENT '活動編號',
  `avt_pre_check`   tinyint(1) NOT NULL COMMENT '活動審核確認',
  PRIMARY KEY (avt_pre_no),
  CONSTRAINT `avt_preview_ibfk_1` FOREIGN KEY (`mem_user`) REFERENCES `member` (`mem_user`),
  CONSTRAINT `avt_preview_ibfk_2` FOREIGN KEY (`avt_no`) REFERENCES `activity` (`avt_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活動審核';

INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('tom887',1,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('winnie',1,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('mary12',2,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('winnie',2,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('tom887',3,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('mary12',3,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('mary12',4,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('winnie',4,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('tom887',5,1);
INSERT INTO `avt_preview`(`mem_user`,`avt_no`,`avt_pre_check`) VALUES ('mary12',5,1);

--
-- Table structure for table `other_data` 報名其他資料
--

DROP TABLE IF EXISTS `other_data`;
CREATE TABLE `other_data` (
  `oth_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '其他編號',
  `oth_name`    varchar(30) NOT NULL COMMENT '其他項目',
  `avt_no`    int(10) NOT NULL COMMENT '活動編號',
  PRIMARY KEY (oth_no),
  CONSTRAINT `other_data_ibfk_1` FOREIGN KEY (`avt_no`) REFERENCES `activity` (`avt_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='報名其他資料';

INSERT INTO `other_data`(`oth_name`,`avt_no`) VALUES ('葷素',1);
INSERT INTO `other_data`(`oth_name`,`avt_no`) VALUES ('監護人姓名',1);
INSERT INTO `other_data`(`oth_name`,`avt_no`) VALUES ('監護人電話',1);
INSERT INTO `other_data`(`oth_name`,`avt_no`) VALUES ('葷素',2);
INSERT INTO `other_data`(`oth_name`,`avt_no`) VALUES ('葷素',5);
INSERT INTO `other_data`(`oth_name`,`avt_no`) VALUES ('監護人姓名',5);
INSERT INTO `other_data`(`oth_name`,`avt_no`) VALUES ('監護人電話',5);

--
-- Table structure for table `signup_avt` 報名人員清單
--

DROP TABLE IF EXISTS `signup_avt`;
CREATE TABLE `signup_avt` (
  `sign_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '報名人員編號',
  `sign_user`    varchar(10) COMMENT '報名人員帳號',
  `sign_name`     varchar(10) NOT NULL COMMENT '報名人員姓名',
  `sign_birth`  date NOT NULL COMMENT '報名人員生日' ,
  `sign_gen`  varchar(1) NOT NULL COMMENT '報名人員性別' ,
  `sign_idn`  varchar(11) NOT NULL COMMENT '報名人員身分證' ,
  `sign_phone`  varchar(11) NOT NULL COMMENT '報名人員手機號碼' ,
  `sign_mail`  varchar(30) COMMENT '報名人員電子信箱' ,
  `sign_addr`  varchar(70) NOT NULL COMMENT '報名人員地址' ,
  `sign_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '報名時間' ,
  `sign_memo`  varchar(70) COMMENT '備註' ,
  `sign_count`  int(2) NOT NULL COMMENT '參與人數' ,
  `mem_user`    varchar(10) NOT NULL COMMENT '會員編號',
  `avt_no`    int(10) NOT NULL COMMENT '活動編號',
  PRIMARY KEY (sign_no),
  CONSTRAINT `signup_avt_ibfk_1` FOREIGN KEY (`mem_user`) REFERENCES `member` (`mem_user`),
  CONSTRAINT `signup_avt_ibfk_2` FOREIGN KEY (`avt_no`) REFERENCES `activity` (`avt_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='報名人員清單';


INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('mary12','林瑪麗','1994-05-20','W','H773556398','0998654345','mary665@gmail.com','台北市大安區羅斯福路四段3號',null,1,'mary12',1);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('john95','陳約翰','1990-07-04','M','A229884653','0923887465','john95450@gmail.com','新北市新莊市建國一路46號',null,3,'john95',1);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
(null,'陳約蛋','1992-10-04','M','A229884658','0923675332','egg65@gmail.com','新北市新莊市建國一路46號',null,0,'john95',1);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
(null,'林美美','1982-07-01','M','A884773684','0923675332','beauty5@gmail.com','新北市新莊市建國一路46號',null,0,'john95',1);



INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('tom887','王湯尼','1986-11-14','M','C884663563','0977445321','susan0922@gmail.com','基隆市暖暖區源遠路22號',null,1,'tom887',2);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('john95','陳約翰','1990-07-04','M','A229884653','0923887465','john95450@gmail.com','新北市新莊市建國一路46號',null,1,'john95',2);



INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('winnie','徐溫尼','1987-08-01','W','H773009465','0987788987','winnie223@gmail.com','新北市三峽區中正路二段400號',null,1,'winnie',3);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('mary12','林瑪麗','1994-05-20','W','H773556398','0998654345','mary665@gmail.com','台北市大安區羅斯福路四段3號',null,2,'mary12',3);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
(null,'劉清清','1984-02-18','W','H8554332987','0954778956','yty667@gmail.com','台北市大安區羅斯福路四段3號',null,0,'mary12',3);



INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('tom887','王湯尼','1986-11-14','M','C884663563','0977445321','susan0922@gmail.com','基隆市暖暖區源遠路22號',null,1,'tom887',4);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('winnie','徐溫尼','1987-08-01','W','H773009465','0987788987','winnie223@gmail.com','新北市三峽區中正路二段400號',null,1,'winnie',4);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('mary12','林瑪麗','1994-05-20','W','H773556398','0998654345','mary665@gmail.com','台北市大安區羅斯福路四段3號',null,2,'mary12',4);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
(null,'劉清清','1984-02-18','W','H8554332987','0954778956','yty667@gmail.com','台北市大安區羅斯福路四段3號',null,0,'mary12',4);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('john95','陳約翰','1990-07-04','M','A229884653','0923887465','john95450@gmail.com','新北市新莊市建國一路46號',null,3,'john95',4);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
(null,'陳約蛋','1992-10-04','M','A229884658','0923675332','egg65@gmail.com','新北市新莊市建國一路46號',null,0,'john95',4);

INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
(null,'林美美','1982-07-01','M','A884773684','0923675332','beauty5@gmail.com','新北市新莊市建國一路46號',null,0,'john95',4);



INSERT INTO `signup_avt` 
(`sign_user`,`sign_name`,`sign_birth`,`sign_gen`,`sign_idn`,`sign_phone`,`sign_mail`,`sign_addr`,`sign_memo`,`sign_count`,`mem_user`,`avt_no`) VALUES 
('winnie','徐溫尼','1987-08-01','W','H773009465','0987788987','winnie223@gmail.com','新北市三峽區中正路二段400號',null,1,'winnie',5);




--
-- Table structure for table `other_data_ans` 報名其他資料填寫
--

DROP TABLE IF EXISTS `other_data_ans`;
CREATE TABLE `other_data_ans` (
  `othea_no`    int(10) NOT NULL AUTO_INCREMENT COMMENT '其他填寫編號',
  `othea_con`     text NOT NULL COMMENT '其他填寫資料',
  `oth_no`    int(10) NOT NULL COMMENT '其他編號',
  `sign_no`    int(10) NOT NULL COMMENT '報名人員編號',
  PRIMARY KEY (othea_no),
  CONSTRAINT `other_data_ans_ibfk_1` FOREIGN KEY (`oth_no`) REFERENCES `other_data` (`oth_no`),
  CONSTRAINT `other_data_ans_ibfk_2` FOREIGN KEY (`sign_no`) REFERENCES `signup_avt` (`sign_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='報名其他資料填寫';

INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('葷',1,1);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('林建隆',2,1);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('0987466783',3,1);

INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('素',1,2);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('陳大功',2,2);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('0933546784',3,2);

INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('素',1,3);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('陳大功',2,3);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('0933546784',3,3);

INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('素',1,4);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('陳大功',2,4);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('0933546784',3,4);

INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('素',4,5);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('葷',4,6);

INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('葷',5,17);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('徐叮噹',6,17);
INSERT INTO `other_data_ans`(`othea_con`,`oth_no`,`sign_no`) VALUES ('0902987463',7,17);
