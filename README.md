 # Projeto Health Check

# Especificações do Projeto
🚀Dados Clínicos a Serem Registrados (Prontuário)
Nome
Temperatura Corporal
Período (em dias) com Tosse
Período (em dias) com Dor de Cabeça
Se Visitou e Há Quantas Semanas os Seguintes Países:
Itália
China
Indonésia
Portugal
Estados Unidos
### Regras de Negócio
```
 O paciente deve ser internado para tratamento se:
Visitou os países assinalados nas últimas 6 semanas, está com tosse e dor de cabeça há mais de 5 dias e está com febre (temperatura maior que 37º).
```
```
O paciente deve ser enviado à quarentena se:
Visitou os países assinalados nas últimas 6 semanas.
Tem mais de 60 anos ou menos de 10 anos de idade e:
Está com febre ou está com dor de cabeça há mais de 3 dias ou está com tosse há mais de 5 dias.
Tem entre 10 e 60 anos e está com febre, além de dor de cabeça e tosse há mais de 5 dias.
```
```
O paciente deve ser liberado se não atender nenhuma das situações anteriores.
```
## 🛠️ Funcionalidades
```
Cadastrar o prontuário (dados clínicos) de um paciente.
```
```
Evitar cadastrar mais de um prontuário para o mesmo paciente.
```
```
Indicar se um paciente em específico deve ser tratado, posto em quarentena ou liberado.
```
```
Listar, separadamente, todos os pacientes que devem ser tratados, postos em quarentena e liberados.
```
## Requisitos
## 🛠️ persistência local da aplicação: RoomDB.
## 🛠️ código na plataforma Java.
