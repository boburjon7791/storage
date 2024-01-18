package com.example.my_mvc_project.services.report_services;

import com.example.my_mvc_project.dtos.reports.MonthlyReportDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class MonthlyReportServiceImpl implements MonthlyReportService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ReportsCheckUtil checkUtil;
    @Override
    public List<MonthlyReportDto> reportsByYear(int year) {
        String sql= """
                select year,month,sum(total) as total
                                      from (select extract(year from date_time) as year,
                                         extract(month from date_time) as month,
                                         sum(sold_price) as total
                                  from selling
                                  where
                                          extract(year from date_time)=:year and
                                      extract(month from date_time) between :start and :end
                                  group by date_time) as t2
                                  group by month, year
                """;
        DecimalFormat df=new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        return checkUtil.checkMonth(namedParameterJdbcTemplate.query(
                sql,
                Map.of(
                        "start", 1,
                        "end", 12,
                        "year", year
                ),
                (rs, rowNum) -> new MonthlyReportDto(
                        rs.getInt("year"),
                        Month.of(rs.getInt("month")),
                        df.format(rs.getDouble("total"))
                )
        ));
    }
}
