package com.calculadora.controller;

import com.calculadora.record.ReportResponseRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.calculadora.record.ReportDataRecord;
import com.calculadora.service.CalculadoraService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculadora")
@CrossOrigin(origins = "*")
public class CalculadoraController
{

	private final CalculadoraService calculadoraService;

	@PostMapping("/calcular")
	public ResponseEntity<ReportResponseRecord> calcular(@RequestBody ReportDataRecord reportDataRecord)
	{
		ReportResponseRecord response = calculadoraService.geraTabela(reportDataRecord);

		return ResponseEntity.ok(response);
	}
}
