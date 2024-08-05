<template>
	<div id="app">
		<h1 style="text-align: left">Calculadora de Empréstimos</h1>
		<CalcForm />
	</div>
</template>

<script>
import CalcForm from './components/CalcForm.vue';

export default {
	name: 'App',
	components: {
		CalcForm,
	},
};
</script>

<style>
#app {
	font-family: Avenir, Helvetica, Arial, sans-serif;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
	text-align: center;
	color: #2c3e50;
	margin-top: 60px;
}
</style>
