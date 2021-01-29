truncate table crawl_data;
update crawl_site set status=1;
update crawl_site set status=0 where id=8;
SELECT * FROM mall.crawl_site;
SELECT * FROM mall.crawl_data;


