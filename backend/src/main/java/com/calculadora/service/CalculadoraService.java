package com.calculadora.service;

import com.calculadora.record.ReportResponseRecord;
import com.calculadora.record.ReportRowRecord;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.calculadora.record.ReportDataRecord;

@Service
public class CalculadoraService
{
	private long numeroParcelas;
	private BigDecimal valorParcela;

	public ReportResponseRecord geraTabela(ReportDataRecord reportDataRecord){
		List<ReportRowRecord> rows = new ArrayList<>();
		rows.add(createFirstRow(reportDataRecord));

		calcularParcelas(reportDataRecord.dataInicial(), reportDataRecord.dataFinal(), reportDataRecord.primeiroPagamento());
		calcularValorParcela(reportDataRecord.valorTotal());

		LocalDate dataCompetencia = reportDataRecord.dataInicial();
		LocalDate dataPagamento = reportDataRecord.primeiroPagamento();

		int numeroParcela = 0;

		while(numeroParcela < numeroParcelas){
			LocalDate dataReferencia = null;
			boolean isPagamento = false;

			if(dataCompetencia.plusMonths(1).isBefore(dataPagamento))
			{
				if(dataCompetencia.getDayOfMonth() == dataCompetencia.lengthOfMonth()){
					dataCompetencia = YearMonth.from(dataCompetencia).plusMonths(1).atEndOfMonth();
				}
				else{
					dataCompetencia = dataCompetencia.withDayOfMonth(dataCompetencia.lengthOfMonth());
					if(dataCompetencia.equals(reportDataRecord.dataFinal())){
						isPagamento = true;
						numeroParcela++;
					}
				}

				dataReferencia = dataCompetencia;

			}else{
				if(dataPagamento.isAfter(reportDataRecord.dataFinal())) dataReferencia = reportDataRecord.dataFinal();
				else dataReferencia = dataPagamento;

				isPagamento = true;
				numeroParcela++;
				dataPagamento = dataPagamento.plusMonths(1);
			}

			rows.add(createRow(rows.get(rows.size() - 1), dataReferencia, numeroParcela, isPagamento, reportDataRecord.taxaJuros()));
		}

		return new ReportResponseRecord(rows);
	}


	private void calcularParcelas(LocalDate dataInicial, LocalDate dataFinal, LocalDate dataPagamento){
		Period period = Period.between(dataInicial, dataFinal);
		numeroParcelas = period.toTotalMonths();
		if(numeroParcelas == 0){
			if(dataFinal.equals(dataPagamento)) numeroParcelas = 1;
			else numeroParcelas = 2;
		}
	}

	//AMORTIZAÇÃO CONSTANTE
	private void calcularValorParcela(BigDecimal saldoDevedor){
		valorParcela = saldoDevedor.divide(BigDecimal.valueOf(numeroParcelas), RoundingMode.HALF_UP);
	}

	private ReportRowRecord createFirstRow(ReportDataRecord reportDataRecord){
		return new ReportRowRecord(
			reportDataRecord.dataInicial(),
			reportDataRecord.valorTotal(),
			reportDataRecord.valorTotal(),
			"",
			BigDecimal.ZERO,
			BigDecimal.ZERO,
			reportDataRecord.valorTotal(),
			BigDecimal.ZERO,
			BigDecimal.ZERO,
			BigDecimal.ZERO
		);
	}


	private ReportRowRecord createRow(ReportRowRecord lastRow, LocalDate dataCompetencia, int numeroParcela, boolean isPagamento, BigDecimal taxaJuros){

		LocalDate ultimaData = lastRow.dataCompetencia();
		BigDecimal ultimoSaldoPrincipal = lastRow.saldo();
		BigDecimal ultimoAcumulado = lastRow.acumulado();

		BigDecimal jurosProvisao = calcularJuros(ultimaData, dataCompetencia, taxaJuros, ultimoSaldoPrincipal, ultimoAcumulado);
		BigDecimal jurosPagos = BigDecimal.ZERO;
		BigDecimal valorParcelaAtual = valorParcela;

		BigDecimal saldoPrincipalAtual = ultimoSaldoPrincipal;

		if(isPagamento){
			BigDecimal auxiliar = ultimoSaldoPrincipal.subtract(valorParcelaAtual);
			if(auxiliar.compareTo(BigDecimal.ZERO) < 0)
			{
				valorParcelaAtual = ultimoSaldoPrincipal;
				saldoPrincipalAtual = BigDecimal.ZERO;
			}else{
				saldoPrincipalAtual = auxiliar;
			}
			jurosPagos = ultimoAcumulado.add(jurosProvisao);
		}

		BigDecimal jurosAcumulado = ultimoAcumulado.add(jurosProvisao).subtract(jurosPagos);

		BigDecimal saldoDevedorAtual = saldoPrincipalAtual.add(jurosAcumulado);

		return new ReportRowRecord(
			dataCompetencia,
			BigDecimal.ZERO,
			saldoDevedorAtual,
			isPagamento ? numeroParcela + "/" + numeroParcelas : "",
			isPagamento ? valorParcelaAtual.add(jurosPagos) : BigDecimal.ZERO,
			isPagamento ? valorParcelaAtual : BigDecimal.ZERO,
			saldoPrincipalAtual,
			jurosProvisao,
			jurosAcumulado,
			jurosPagos
		);

	}

	private BigDecimal calcularJuros(LocalDate ultimaData, LocalDate dataCompetencia,
		BigDecimal taxaJuros, BigDecimal ultimoSaldoPrincipal, BigDecimal ultimoAcumulado)
	{

		Period period = Period.between(ultimaData, dataCompetencia);
		double dias = (double) period.getDays() /360;
		BigDecimal juros1 = BigDecimal.valueOf(
			Math.pow(taxaJuros.add(BigDecimal.valueOf(1)).doubleValue(), dias)).subtract(BigDecimal.ONE);

		return (ultimoSaldoPrincipal.add(ultimoAcumulado).multiply(juros1)).setScale(2, RoundingMode.HALF_DOWN);
	}
}
