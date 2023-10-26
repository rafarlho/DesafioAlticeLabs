# DesafioAlticeLabs

Desafio proposto pela Altice Labs, reecaminhado pela empresa Dellent.


Com face ao desafio proposto, a lista de ficheiros correspondestes é a seguinte:

->DesafioAltice/

	->src/
		->ChargingHandlerTest.java
		->OperatorTest.java
		->TelecomComp/
			->BillingAccount.java
			->ChargingRequest.java
			->ChargingReply.java
			->ClientDataRecords.java
			->ChargingHandler.java
			->Operator.java

 - BillingAccount.java
Classe que representa uma conta de um utilizador. Tem a informação do número, tarifários de ambos os serviços, buckets e counters.

- CharginRequest.java
Classe que representa um pedido de cobrança para o número de um respetivo utilizador. Tem informação da data da cobrança definida como Date, do número a cobrar, do serviço a utilizar, se é roaming, e número de RSUs. Tem também um identificador ID sequencial e que incrementa após cada pedido executado.

- CharginReply.java
Esta classe representa uma resposta, afirmativa ou negativa, a um pedido de cobrança. Contém o ID, igual ao pedido de cobrança, o resultado e o número de GSUs.

- ClientDataRecord.java
Representa um registo de uma cobrança feita. Contém o número da transação, a data do request, o serviço e o respetivo tarifário cobrado, o ID do ChargingRequest e o resultado do consequente ChargingReply. 
Apresenta ainda os valores dos buckets e counters do utilizador na data da transação.

- ChargingHandler.java
Esta classe tem como objetivo lidar com um pedido de cobrança, calcular a elegiibilidade e o custo, determinar o charging reply e gerar o respetivo CDR.
Em detalhe o método ChargingHandler(ChargingRequest request, BillingAccount billingAccount) inicialmente cria um nova ChargingReply sem resultado e define GSU como 0. 
Após isto, a classe dispõe de 6 funções para lidarem com cada tarifário : HandleAlphaX(ChargingRequest, BillingAccount) e HandleBetaX(ChargingRequest, BillingAcount) que calculam em função do serviço e do respetivo tarifário, se o utilizador pode ou não executar o pedido e emite o resultado, debitando o custo se for o caso.
Concluido esta operação, o método ChargingHandler() define o resultado do CDR e guarda-o, juntamente com o ChargingReply.

- Operator.java
Esta classe funciona como uma strutura de dados como uma operadora, com capacidade de fazer cobranças e verificar os CDRs e as BillingAccounts de todos os utilizadores.
Temo como métodos setBillingAccount(Billing Account) que cria e guarda num HashMap o número e a respetiva BillingAccount e makeRequest(ChargingRequest), que recebe um pedido de cobrança, verifica se é legitimo com base no registo das BillingAccounts e submete o pedido para para o ChargingHandler o tratar. Após isto adiciona o respetivo CDR à lista dos registos do respetivo número e ordena esta lista com base no pedido mais recente.
Caso não exista o número do pedido nos billings records, retorna uma mensagem a indicar o sucecido.

- OperatorTest.java
Teste unitário aplicado à classe Operator, recorrendo a Junit5, que testa o Contrutor da classe e inicialização dos HashMaps de BillingAccounts e CDRs, a adição de uma BillingAccount ao respetivo HashMap e a efetuação de um pedido, mais especificamente, a adição de CDRs de um utilizador ao respetivo HashMap.

- CharginHandlerTest.java
Classe de testes que testa a cobrança dos vários tarifários e dos valores aplicáveis possíveis. Esta classe testa todos os tarifários em diferentes condições e com diferentes valores nos buckets, assegurando que a cobrança efetuada é sempre correta.

		
