package com.yinhai.rpt.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface MapperDAO {
    void insert_out_mdtrt_stt_e(List<Map> list);
    void insert_detl_ana_kpi_rslt_d(List<Map> list);
    List<Map> select(@Param("no") String no, @Param("year") String year);
//    String version();
}
