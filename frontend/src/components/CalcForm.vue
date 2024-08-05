<template>
	<div>
		<div>
			<label for="dataInicial">Data Inicial:</label>
			<input
				type="date"
				id="dataInicial"
				name="dataInicial"
				v-model="formData.dataInicial"
			/>
			<label for="dataFinal">Data final:</label>
			<input
				type="date"
				id="dataFinal"
				name="dataFinal"
				v-model="formData.dataFinal"
			/>
			<label for="primeiroPag">Primeiro Pagamento:</label>
			<input
				type="date"
				id="primeiroPag"
				name="primeiroPag"
				v-model="formData.primeiroPagamento"
			/>
			<label for="valorEmprestimo">Valor de Empréstimo:</label>
			<input
				type="text"
				id="primeiroPag"
				name="primeiroPag"
				v-model="formData.valorTotal"
			/>
			<label for="taxaJuros">Taxa de Juros:</label>
			<input
				type="text"
				id="taxaJuros"
				name="taxaJuros"
				v-model="taxaJuros"
				style="width: 2%"
			/>
			<button @click="calcular()">Calcular</button>
		</div>
		<TabelaEmprestimos v-if="this.data" :data="this.data"></TabelaEmprestimos>
	</div>
</template>

<script>
import axios from 'axios';
import TabelaEmprestimos from './TabelaEmprestimos.vue';

export default {
	components: {
		TabelaEmprestimos,
	},
	data() {
		return {
			formData: {
				dataInicial: '',
				dataFinal: '',
				primeiroPagamento: '',
				valorTotal: 0,
				taxaJuros: '',
			},
			data: null,
			taxaJuros: 0,
		};
	},
	methods: {
		calcular() {
			if (!this.validarCampos()) return;

			if (this.formData.dataInicial > this.formData.dataFinal) {
				alert('Data inicial não pode ser maior que a data final');
				return;
			}
			if (this.formData.dataInicial > this.formData.primeiroPagamento) {
				alert('O pagamento deve ser após a data inicial');
				return;
			}
			if (this.formData.primeiroPagamento > this.formData.dataFinal) {
				alert('O pagamento deve ser antes da data final');
				return;
			}

			this.formData.taxaJuros = this.taxaJuros / 100;

			axios
				.post('http://localhost:8080/calculadora/calcular', this.formData)
				.then((response) => {
					this.data = response.data;
				});
		},
		validarCampos() {
			if (this.formData.dataInicial === '') {
				alert('Data inicial é obrigatória');
				return false;
			}
			if (this.formData.dataFinal === '') {
				alert('Data final é obrigatória');
				return false;
			}
			if (this.formData.primeiroPagamento === '') {
				alert('Data do primeiro pagamento é obrigatória');
				return false;
			}
			if (this.formData.valorTotal === '') {
				alert('Valor do empréstimo é obrigatório');
				return false;
			}
			if (this.taxaJuros === '') {
				alert('Taxa de juros é obrigatória');
				return false;
			}

			return true;
		},
	},
};
</script>

<style>
input {
	margin: 12px;
}

button {
	margin-left: 50px;
	background-color: #546bd6;
	color: white;
	border: none;
	border-radius: 7px;
	width: 5%;
	height: 30px;
}
</style>
