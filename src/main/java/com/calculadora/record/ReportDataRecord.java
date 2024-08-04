package com.calculadora.record;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReportDataRecord(
	LocalDate dataInicial,
	LocalDate dataFinal,
	LocalDate primeiroPagamento,
	BigDecimal valorTotal,
	BigDecimal taxaJuros
)
{
}
