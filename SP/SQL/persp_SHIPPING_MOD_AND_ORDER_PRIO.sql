-- readable format
CREATE MATERIALIZED VIEW SHIPPING_MOD_AND_ORDER_PRIO 
  BUILD IMMEDIATE 
  REFRESH FAST 
as 
select 
  l_shipmode, 
  sum(
	case 
	  when 
	    o_orderpriority ='1-URGENT' or o_orderpriority ='2-HIGH' 
	  then 
	    1 
	  else 
	    0 
	end) as high_line_count, 
  sum(
	case 
	  when 
	    o_orderpriority <> '1-URGENT' and o_orderpriority <> '2-HIGH' 
	  then 
	    1 
	  else 
	    0 
	end) as low_line_count, 
  count(
	case 
	  when 
	    o_orderpriority ='1-URGENT' or o_orderpriority ='2-HIGH' 
	  then 
	    1 
	  else 
	    0 
	end), 
  count(
	case 
	  when 
	    o_orderpriority <> '1-URGENT' and o_orderpriority <> '2-HIGH' 
	  then 
	    1 
	  else 
	    0 
	end) 
from 
  h_order, 
  h_lineitem 
where 
  o_orderkey = l_orderkey 
and 
  l_shipmode in ('MAIL', 'SHIP') 
and 
  l_commitdate < l_receiptdate 
and 
  l_shipdate < l_commitdate 
and 
  l_receiptdate >= to_date('1994-01-01','YYYY-MM-DD') 
and 
  l_receiptdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year 
group by 
  l_shipmode 
order by 
  l_shipmode;


-- sqlplus create mv single line command --
CREATE MATERIALIZED VIEW SHIPPING_MOD_AND_ORDER_PRIO BUILD IMMEDIATE REFRESH FAST as select l_shipmode, sum(case when o_orderpriority ='1-URGENT' or o_orderpriority ='2-HIGH' then 1 else 0 end) as high_line_count, sum(case when o_orderpriority <> '1-URGENT' and o_orderpriority <> '2-HIGH' then 1 else 0 end) as low_line_count, count(case when o_orderpriority ='1-URGENT' or o_orderpriority ='2-HIGH' then 1 else 0 end), count(case when o_orderpriority <> '1-URGENT' and o_orderpriority <> '2-HIGH' then 1 else 0 end) from h_order, h_lineitem where o_orderkey = l_orderkey and l_shipmode in ('MAIL', 'SHIP') and l_commitdate < l_receiptdate and l_shipdate < l_commitdate and l_receiptdate >= to_date('1994-01-01','YYYY-MM-DD') and l_receiptdate < to_date('1994-01-01','YYYY-MM-DD') + interval '1' year group by l_shipmode order by l_shipmode;


-- explain why couldn't be as fast refresh--
exec dbms_mview.explain_mview('select l_shipmode, sum(case when o_orderpriority =''1-URGENT'' or o_orderpriority =''2-HIGH'' then 1 else 0 end) as high_line_count, sum(case when o_orderpriority <> ''1-URGENT'' and o_orderpriority <> ''2-HIGH'' then 1 else 0 end) as low_line_count, count(case when o_orderpriority =''1-URGENT'' or o_orderpriority =''2-HIGH'' then 1 else 0 end), count(case when o_orderpriority <> ''1-URGENT'' and o_orderpriority <> ''2-HIGH'' then 1 else 0 end) from h_order, h_lineitem where o_orderkey = l_orderkey and l_shipmode in (''MAIL'', ''SHIP'') and l_commitdate < l_receiptdate and l_shipdate < l_commitdate and l_receiptdate >= to_date(''1994-01-01'',''YYYY-MM-DD'') and l_receiptdate < to_date(''1994-01-01'',''YYYY-MM-DD'') + interval ''1'' year group by l_shipmode order by l_shipmode','6');

select capability_name, possible, related_text, msgtxt from mv_capabilities_table where statement_id='6';


