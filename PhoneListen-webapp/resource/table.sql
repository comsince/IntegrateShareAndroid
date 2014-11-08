create table user_info(
	ui_id INTEGER primary key not null auto_increment, 
	user_name VARCHAR(40) not null,
	user_email VARCHAR(40), 
	user_password VARCHAR(40) not null,
	user_province VARCHAR(40),
	user_city VARCHAR(40),
	user_zone VARCHAR(40),
	user_address VARCHAR(80),
	user_gender INTEGER,
	user_age INTEGER,
	user_regtime DATETIME not null,
	user_phonenumber VARCHAR(20) not null,
	user_origin INTEGER not null,
	user_referee VARCHAR(40),
	user_pushable INTEGER not null,
	user_payment_password VARCHAR(40),
	user_withdraw_account VARCHAR(40),
	user_imei VARCHAR(15) not null,
	user_birthday VARCHAR(40)
	);
create table user_credit(
	uc_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	credit_total_revenue INTEGER not null,
	credit_total_expenditure INTEGER not null,
	user_level INTEGER not null,
	credit_remaining INTEGER not null
	);
create table credit_sync(
	cs_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	sync_time DATETIME not null,
	sync_area VARCHAR(80) not null,
	sync_credit INTEGER not null,
	credit_accumulate_time INTEGER not null
	);
create table user_position(
	up_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	current_area VARCHAR(80) not null,
	current_longitude DOUBLE(10,6) not null,
	current_latitude DOUBLE(10,6) not null
	);
create table user_login(
	ul_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	log_type INTEGER not null,
	log_time DATETIME not null,
	log_longitude DOUBLE(10,6),
	log_latitude DOUBLE(10,6)
	);
create table ads_info(
	ai_id INTEGER primary key not null auto_increment, 
	ad_type INTEGER not null,
	ad_owner_id INTEGER not null, 
	ad_share_info TEXT,
	ad_link_info VARCHAR(120),
	ad_level INTEGER not null,
	ad_brand VARCHAR(40) not null,
	ad_submit_time DATETIME not null,
	ad_start_time DATETIME not null,
	ad_end_time DATETIME not null,
	ad_format INTEGER not null,
	ad_portrait VARCHAR(120),
	ad_abstraction TEXT,
	ad_picture VARCHAR(120),
	ad_video VARCHAR(120),
	ad_description TEXT,
	ad_phone_number VARCHAR(20),
	ad_category INTEGER,
	ad_html VARCHAR(120)
	);
create table ls_param(
	lsp_id INTEGER primary key not null auto_increment,
	ad_id INTEGER not null,
	ls_expected_credit INTEGER, 
	ls_cutoff_scores INTEGER,
	ls_weight INTEGER,
	ls_limited_credit INTEGER not null,
	ls_launch_type INTEGER not null,
	right_slide_credit INTEGER not null,
	right_slide_limitation INTEGER,
	left_slide_credit INTEGER not null,
	left_slide_limitation INTEGER,
	action_type INTEGER not null,
	action_credit INTEGER not null,
	action_limitation INTEGER,
	user_reward_limitation INTEGER not null,
	user_reward_unittime INTEGER
	);
create table ac_param(
	acp_id INTEGER primary key not null auto_increment,
	ad_id INTEGER not null,
	ac_expected_credit INTEGER, 
	ac_cutoff_scores INTEGER,
	ac_weight INTEGER,
	ac_limited_credit INTEGER not null,
	ac_launch_type INTEGER not null,
	share_credit INTEGER not null,
	share_limitation INTEGER,
	watch_credit INTEGER not null,
	watch_limitation INTEGER,
	user_reward_limitation INTEGER not null
	);
create table ls_statistics(
	lss_id INTEGER primary key not null auto_increment,
	lsp_id INTEGER not null,
	ad_id INTEGER not null,
	ls_assigned_credit INTEGER not null,
	ls_assigned_users INTEGER not null,
	ls_paid_credit INTEGER not null,
	ls_exposured_times INTEGER not null,
	ls_actioned_times INTEGER not null
	);
create table ac_statistics(
	acs_id INTEGER primary key not null auto_increment,
	acp_id INTEGER not null,
	ad_id INTEGER not null,
	ac_assigned_credit INTEGER not null,
	ac_assigned_users INTEGER not null,
	ac_paid_offline_credit INTEGER not null,
	ac_watched_times INTEGER not null,
	ac_shared_times INTEGER not null,
	ac_paid_online_credit INTEGER not null
	);
create table ad_history(
	ah_id INTEGER primary key not null auto_increment,
	ad_id INTEGER not null,
	ad_platform_type INTEGER not null,
	lsp_id INTEGER not null,
	acp_id INTEGER not null,
	ac_expected_credit INTEGER,
	ac_limited_credit INTEGER,
	ac_launch_type INTEGER not null,
	ac_assigned_credit INTEGER not null,
	ac_paid_credit INTEGER not null,
	ad_exposured_times INTEGER not null,
	ad_actioned_times INTEGER,
	ad_watched_times INTEGER not null,
	ad_shared_times INTEGER not null,
	ad_start_time DATETIME,
	ad_end_time DATETIME
	);
create table credit_op(
	cc_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	ad_id INTEGER not null,
	cc_time DATETIME not null,
	cc_credit INTEGER not null,
	cc_platform INTEGER not null,
	cc_type INTEGER not null
	);
create table user_ad_associated(
	ua_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	ad_id INTEGER not null,
	ua_platform_type INTEGER not null,
	ls_credit INTEGER not null,
	ac_credit INTEGER not null
	);
create table commodity_info(
	ci_id INTEGER primary key not null auto_increment, 
	commodity_name VARCHAR(40) not null,
	commodity_credit INTEGER not null, 
	commodity_type INTEGER not null,
	commodity_provider INTEGER not null,
	commodity_level INTEGER not null,
	commodity_brand VARCHAR(40) not null,
	commodity_submit_time DATETIME not null,
	commodity_begin_time DATETIME not null,
	commodity_end_time DATETIME not null,
	commodity_format INTEGER not null,
	commodity_portrait VARCHAR(120),
	commodity_picture VARCHAR(120),
	commodity_description TEXT,
	commodity_share_info TEXT,
	commodity_link_info VARCHAR(120),
	commodity_coupon INTEGER not null
	);
create table coupon_repository(
	cr_id INTEGER primary key not null auto_increment, 
	commodity_id INTEGER not null,
	coupon_code VARCHAR(80) not null,
	coupon_start_time DATETIME not null,
	coupon_end_time DATETIME not null,
	coupon_status INTEGER not null
	);
create table user_coupon_associated(
	uc_id INTEGER primary key not null auto_increment, 
	user_id INTEGER not null,
	coupon_id INTEGER not null
	);
create table coupon_exchanged(
	ce_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	coupon_id INTEGER not null,
	exchange_time DATETIME not null,
	exchange_credit INTEGER not null,
	discount_level INTEGER not null
	);
create table coupon_used(
	cu_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	coupon_id INTEGER not null,
	used_time DATETIME not null
	);
create table withdraw_op(
	wo_id INTEGER primary key not null auto_increment,
	withdraw_id INTEGER not null,
	wo_time DATETIME not null,
	wo_type INTEGER not null
	);
create table withdraw_info(
	wi_id INTEGER primary key not null auto_increment,
	user_id INTEGER not null,
	withdraw_account VARCHAR(40) not null,
	withdraw_credit INTEGER not null,
	withdraw_request_time DATETIME not null,
	withdraw_status INTEGER not null
	);
create table merchant_info(
	mi_id INTEGER primary key not null auto_increment,
	merchant_name VARCHAR(40) not null,
	merchant_address VARCHAR(120) not null,
	merchant_linkman VARCHAR(20) not null,
	merchant_phone VARCHAR(20) not null,
	merchant_license VARCHAR(20) not null
	);
create table app_setting(
	as_id INTEGER primary key not null auto_increment,
	as_name VARCHAR(40) not null,
	as_type VARCHAR(40) not null,
	as_value VARCHAR(80) not null
	);



create table T_UPDATEINFO(
	versionCode int,
	versionName varchar(16),
	appUrl varchar(100) not null,
	message  varchar(320),
	updatetime varchar(20)
 
)DEFAULT CHARSET=utf8;