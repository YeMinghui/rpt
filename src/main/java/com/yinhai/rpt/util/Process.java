package com.yinhai.rpt.util;

import com.yinhai.rpt.mapper.MapperDAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Process {

    @Autowired
    MapperDAO dao;

    public BigDecimal num(Map map, String key) {
        Object obj = map.get("data");
        if (obj != null) {
            double asDouble = ((List<Map>) obj).stream().filter(i -> key.equals(i.get("loc")))
                .mapToDouble(i -> Double.parseDouble((String) i.get("content"))).findAny().getAsDouble();
            return BigDecimal.valueOf(asDouble);
        }
        return BigDecimal.ZERO;
    }

    public List<Map> select(String ... args) {
        List<Map> result = new ArrayList<>();
        for (int i = 0; i < args.length; i+=2) {
            result.addAll(dao.select(args[i], args[i+1]));
        }
        return result;
    }

    // 异地就医
    public void ydjy () {
        List<Map> source = select("HI8.1", "2020", "HI8", "2020", "HI9.1", "2019", "HI9", "2019");
        List<Map> result = new ArrayList<>();
        List<Map> detlKpis = new ArrayList<>();

        source.stream().filter(rpt -> Arrays.asList("HI8", "HI9").contains(rpt.get("sheetNo")))
            .forEach(rpt -> {
                BigDecimal mzrc, zyrc, rs, mzfy, zyfy;
                if ("2020".equals(rpt.get("year"))) {
                    // 处理2020年的报表
                    // 职工异地就医人次
                    mzrc = num(rpt, "L14").add(num(rpt, "T14"));
                    zyrc = num(rpt, "AB14");
                    // 职工异地就医费用
                    mzfy = num(rpt, "E14").add(num(rpt, "M14"));
                    zyfy = num(rpt, "U14");
                    // 职工异地就医人数
                    rs = num(rpt, "D14");
                } else {
                    // 处理2019年的报表
                    // 职工异地就医人次
                    mzrc = num(rpt, "L14").add(num(rpt, "T14"));
                    zyrc = num(rpt, "AB14");
                    // 职工异地就医费用
                    mzfy = num(rpt, "E14").add(num(rpt, "M14"));
                    zyfy = num(rpt, "U14");
                    // 职工异地就医人数
                    rs = num(rpt, "D14");
                }

                // 职工门诊
                HashMap<String, Object> data = new HashMap<>();
                data.put("STT_YEAR", rpt.get("year"));
                data.put("STT_MON", rpt.get("mon"));
                data.put("ADMDVS", rpt.get("admdvs"));
                data.put("POOLAREA_NO", rpt.get("admdvs"));
                data.put("FUND_INSUTYPE", "1"); // 职工1   居民2
                data.put("MED_MDTRT_TYPE", "1");  // 门诊1  住院2
                if ("520000".equals(rpt.get("admdvs"))) {
                    data.put("OUT_FEE_TYPE", 4);  // 2 4  报表统计的都是本地参保人员去异地就医
                } else {
                    data.put("OUT_FEE_TYPE", 2);
                }
                data.put("MDTRT_PSNTIME", mzrc);
                data.put("MDTRT_FEE", mzfy);
                result.add(data);
                // 职工住院
                data = new HashMap<>();
                data.put("STT_YEAR", rpt.get("year"));
                data.put("STT_MON", rpt.get("mon"));
                data.put("ADMDVS", rpt.get("admdvs"));
                data.put("POOLAREA_NO", rpt.get("admdvs"));
                data.put("FUND_INSUTYPE", "1"); // 职工1   居民2
                data.put("MED_MDTRT_TYPE", "2");  // 门诊1  住院2
                if ("520000".equals(rpt.get("admdvs"))) {
                    data.put("OUT_FEE_TYPE", 4);  // 2 4  报表统计的都是本地参保人员去异地就医
                } else {
                    data.put("OUT_FEE_TYPE", 2);
                }
                data.put("MDTRT_PSNTIME", zyrc);
                data.put("MDTRT_FEE", zyfy);
                result.add(data);

                //DLKPI0027 异地就医人数
                HashMap<String, Object> zgrs = new HashMap<>();
                zgrs.put("STT_YEAR", rpt.get("year"));
                zgrs.put("STT_MON", rpt.get("mon"));
                zgrs.put("ADMDVS", rpt.get("admdvs"));
                zgrs.put("POOLAREA_NO", rpt.get("admdvs"));
                zgrs.put("FUND_INSUTYPE", "1");
                zgrs.put("DETL_KPI_ID", "DLKPI0027");
                zgrs.put("DETL_KPI_RSLT", rs);
                detlKpis.add(zgrs);
            });

        source.stream().filter(rpt -> Arrays.asList("HI8.1", "HI9.1").contains(rpt.get("sheetNo")))
            .forEach(rpt -> {
                BigDecimal mzrc, zyrc, rs, mzfy, zyfy;
                if ("2020".equals(rpt.get("year"))) {
                    // 居民异地就医人次
                    mzrc = num(rpt, "E14").add(num(rpt, "J14"));
                    zyrc = num(rpt, "Q14");
                    // 居民异地就医费用
                    mzfy = num(rpt, "G14").add(num(rpt, "K14"));
                    zyfy = num(rpt, "S14");
                    // 居民异地就医人数
                    rs = num(rpt, "D14");
                } else {
                    // 居民异地就医人次
                    mzrc = num(rpt, "E14").add(num(rpt, "J14"));
                    zyrc = num(rpt, "Q14");
                    // 居民异地就医费用
                    mzfy = num(rpt, "G14").add(num(rpt, "K14"));
                    zyfy = num(rpt, "S14");
                    // 居民异地就医人数
                    rs = num(rpt, "D14");
                }

                // 居民门诊
                HashMap<String, Object> data = new HashMap<>();
                data.put("STT_YEAR", rpt.get("year"));
                data.put("STT_MON", rpt.get("mon"));
                data.put("ADMDVS", rpt.get("admdvs"));
                data.put("POOLAREA_NO", rpt.get("admdvs"));
                data.put("FUND_INSUTYPE", "2"); // 职工1   居民2
                data.put("MED_MDTRT_TYPE", "1");  // 门诊1  住院2
                if ("520000".equals(rpt.get("admdvs"))) {
                    data.put("OUT_FEE_TYPE", 4);  // 2 4  报表统计的都是本地参保人员去异地就医
                } else {
                    data.put("OUT_FEE_TYPE", 2);
                }
                data.put("MDTRT_PSNTIME", mzrc);
                data.put("MDTRT_FEE", mzfy);
                result.add(data);
                // 居民住院
                data = new HashMap<>();
                data.put("STT_YEAR", rpt.get("year"));
                data.put("STT_MON", rpt.get("mon"));
                data.put("ADMDVS", rpt.get("admdvs"));
                data.put("POOLAREA_NO", rpt.get("admdvs"));
                data.put("FUND_INSUTYPE", "2"); // 职工1   居民2
                data.put("MED_MDTRT_TYPE", "2");  // 门诊1  住院2
                if ("520000".equals(rpt.get("admdvs"))) {
                    data.put("OUT_FEE_TYPE", 4);  // 2 4  报表统计的都是本地参保人员去异地就医
                } else {
                    data.put("OUT_FEE_TYPE", 2);
                }
                data.put("MDTRT_PSNTIME", zyrc);
                data.put("MDTRT_FEE", zyfy);
                result.add(data);

                //DLKPI0027 异地就医人数
                HashMap<String, Object> zgrs = new HashMap<>();
                zgrs.put("STT_YEAR", rpt.get("year"));
                zgrs.put("STT_MON", rpt.get("mon"));
                zgrs.put("ADMDVS", rpt.get("admdvs"));
                zgrs.put("POOLAREA_NO", rpt.get("admdvs"));
                zgrs.put("FUND_INSUTYPE", "1");
                zgrs.put("DETL_KPI_ID", "DLKPI0027");
                zgrs.put("DETL_KPI_RSLT", rs);
                detlKpis.add(zgrs);
            });

        dao.insert_out_mdtrt_stt_e(result);
        dao.insert_detl_ana_kpi_rslt_d(detlKpis);
    }

}
