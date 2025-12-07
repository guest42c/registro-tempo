# Sistema de Registro de Tempo (Time Tracking)

Este projeto foi desenvolvido como trabalho final da disciplina de Programação Orientada a Objetos, visando aplicar os principais conceitos da POO em Java: Classes, Encapsulamento, Herança, Polimorfismo e Composição.

1. Contexto e Importância
O gerenciamento eficaz do tempo é crucial em ambientes de desenvolvimento de software e projetos em geral. A capacidade de registrar com precisão o tempo gasto em diferentes tarefas permite:
* Monitoramento de Produtividade: Avaliar a eficiência da equipe e identificar gargalos.
* Faturamento e Custo: Fornecer dados precisos para o faturamento de clientes ou para o cálculo do custo real de desenvolvimento.
* Planejamento de Projetos: Melhorar as estimativas futuras com base em dados históricos de tempo.

2. Objetivo do Software
O objetivo principal deste software é fornecer uma ferramenta simples e funcional para registrar o tempo dedicado a diferentes tipos de tarefas, especificamente Product Backlog Items (PBI) e Bugs, utilizando uma interface gráfica intuitiva para o cadastro de dados e uma coleção em memória para o armazenamento.

3. Funcionalidades Implementadas
O sistema permite que o usuário realize as seguintes operações através da interface gráfica (InicioJanela):
* Cadastro de Tarefas: Inclusão de novos itens de trabalho, que podem ser do tipo PBI ou Bug;
* Consulta e edição de Tarefas: Clique nos itens da lista permitem consulta e posteriormente editar as informações das Tarefas. A tarefa é identificada pelo título (alterar o título significa incluir uma nova tarefa);
* Remover tarefas: Excluir tarefas das listas de tarefas.
* Registro de Tempo: Associação de uma quantidade de tempo gasto em uma Tarefa específica. A contabilização de tempo gasto é uma lista de registros de intervalos de trabalho na tarefa.

4. Arquitetura Orientada a Objetos (POO)
O design do sistema é baseado na aplicação dos conceitos de POO:

4.1 Herança e Polimorfismo
* Classe Pai Genérica: Tarefa define atributos e comportamentos comuns a todos os itens de trabalho.
* Classes Filhas Específicas: PBI e Bug herdam da classe pai Tarefa, adicionando atributos próprios (ex: prioridade de Bug) e sobrescrevendo método toString() (Polimorfismo).
* Coleção Polimórfica: A classe principal utiliza uma coleção do tipo da classe pai (ex: List<Tarefa>) para armazenar e gerenciar tanto objetos PBI quanto Bug, ilustrando o Polimorfismo.

4.2 Encapsulamento
Todos os atributos críticos das classes de entidade (Tarefa, ProductBacklogItem, Bug, Registro) são definidos como privados (private), e o acesso e a modificação de dados são estritamente controlados por métodos públicos (public) getters e setters. Da mesma forma métodos internos da classe são encapsulados com privado e métodos públicos são expostos para uso externo.

4.3 Composição
O conceito de Composição é aplicado na classe RegistroDeTempo (o programa principal), que é responsável por gerenciar e "possuir" a coleção (List) de todas as entidades de tempo e atividades do sistema. Além disso, uma entrada de Registro pode compor (ter como atributo) uma referência à sua Atividade associada.

4.4 Tratamento de eventos
Os eventos são tratados na classe da janela principal InicioJanela. Interações de botões, combobox e fechamento do programa, controlam as funcionalidades do sistema.

