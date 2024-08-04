package com.calculadora.record;

import java.util.List;

public record ReportResponseRecord(
	List<ReportRowRecord> rows
)
{
}
