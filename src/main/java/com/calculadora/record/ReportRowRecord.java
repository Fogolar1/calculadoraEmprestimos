package com.calculadora.record;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReportRowRecord(
	LocalDate dataCompetencia,
	BigDecimal valorEmprestimo,
	BigDecimal saldoDevedor,
	String consolidada,
	BigDecimal total,
	BigDecimal amortizacao,
	BigDecimal saldo,
	BigDecimal provisao,
	BigDecimal acumulado,
	BigDecimal pago
)
{
}
