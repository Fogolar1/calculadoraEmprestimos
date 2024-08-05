<template>
	<div class="scroll">
		<table>
			<thead>
				<tr>
					<th colspan="3">Empréstimo</th>
					<th colspan="2">Parcela</th>
					<th colspan="2">Principal</th>
					<th colspan="3">Juros</th>
				</tr>
				<tr>
					<th>Data Competência</th>
					<th>Valor de Empréstimo</th>
					<th>Saldo Devedor</th>
					<th>Consolidada</th>
					<th>Total</th>
					<th>Amortização</th>
					<th>Saldo</th>
					<th>Provisão</th>
					<th>Acumulado</th>
					<th>Pago</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="row in this.data.rows" :key="row.id">
					<td>{{ row.dataCompetencia }}</td>
					<td>{{ row.valorEmprestimo }}</td>
					<td>{{ row.saldoDevedor }}</td>
					<td>{{ row.consolidada }}</td>
					<td>{{ row.total }}</td>
					<td>{{ row.amortizacao }}</td>
					<td>{{ row.saldo }}</td>
					<td>{{ row.provisao }}</td>
					<td>{{ row.acumulado }}</td>
					<td>{{ row.pago }}</td>
				</tr>
			</tbody>
		</table>
	</div>
</template>

<script>
export default {
	props: {
		data: {
			type: Object,
		},
	},
};
</script>

<style>
table {
	width: 100%;
	border-collapse: collapse;
	border: 1px solid black;
}

th {
	border: 1px solid white;
	color: white;
	background-color: #546bd6;
	position: sticky;
	top: 0;
	z-index: 1;
}

td {
	border: 1px solid black;
}

th,
td {
	padding: 8px;
}

.scroll {
	height: 600px;
	overflow: scroll;
}
</style>
