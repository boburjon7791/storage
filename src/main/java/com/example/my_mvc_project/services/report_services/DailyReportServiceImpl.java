package com.example.my_mvc_project.services.report_services;

import com.example.my_mvc_project.dtos.reports.DailyReportDto;
import com.example.my_mvc_project.dtos.reports.DailyReportRequestDto;
import com.example.my_mvc_project.enums.MonthCopy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class DailyReportServiceImpl implements DailyReportService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ReportsCheckUtil checkUtil;
    @Override
    public List<DailyReportDto> reportsByMonth(DailyReportRequestDto dto) {
        String sql= """
                select date,sum(total) as total from (select date(date_time) as date,sum(sold_price) as total from selling
                where extract(month from date_time)= :month and
                extract(year from date_time)=:year
                group by date_time
                order by date_time desc) as t
                group by date
                order by date desc
                """;
        return checkUtil.checkDay(namedParameterJdbcTemplate.query(
                sql, Map.of(
                        "month",dto.month(),
                        "year",dto.year()
                ), (rs, rowNum) -> {
                    LocalDateTime localDateTime = rs.getTimestamp("date").toLocalDateTime();
                    return new DailyReportDto(
                            localDateTime,
                            rs.getDouble("total"), MonthCopy.intValue(localDateTime.getMonthValue())
                    );
                }
        ));
    }
}
