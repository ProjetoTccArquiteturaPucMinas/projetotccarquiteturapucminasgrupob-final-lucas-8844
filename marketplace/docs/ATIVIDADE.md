# Atividade de Implementação da Feature

## 1. Contexto
Este projeto faz parte de um experimento acadêmico para avaliar esforço de compreensão e esforço de modificação em diferentes arquiteturas de software.

Você recebeu a versão do sistema implementada com arquitetura em camadas.

---

## 2. Identificação da Arquitetura
Arquitetura em camadas.

---

## 3. Justificativa da Arquitetura
A estrutura do projeto segue claramente a arquitetura em camadas, organizada da seguinte forma:

- Camada de Apresentação (Presentation): localizada em `presentation/controller/`, responsável pela exposição de endpoints REST (ex.: `CarrinhoController`, `ViewController`).

- Camada de Aplicação (Application): localizada em `application/usecase/` e `application/dto/`, contendo os casos de uso (ex.: `CalcularCarrinhoUseCase`) e DTOs para transferência de dados.

- Camada de Domínio (Domain): localizada em `domain/model/` e `domain/repository/`, responsável pelas entidades de negócio (ex.: `Produto`, `ResumoCarrinho`) e interfaces de repositório.

- Camada de Infraestrutura (Infrastructure): localizada em `infrastructure/repository/`, contendo as implementações dos repositórios (ex.: `InMemoryProdutoRepositorio`).

Essa separação garante baixo acoplamento, alta coesão e facilita a manutenção e testes.

---

## 4. Classe Alterada
A classe alterada foi `CalcularCarrinhoUseCase`, localizada em `application/usecase/`.

Ela é responsável pela lógica de cálculo do carrinho, incluindo subtotal, descontos e total.

---

## 5. Implementação da Feature
A implementação foi realizada no método `executar` da classe `CalcularCarrinhoUseCase`.

Regras implementadas:

- Cálculo do subtotal: soma de (preço × quantidade) de cada item.

- Desconto por quantidade:
  - 1 item → 0%
  - 2 itens → 5%
  - 3 itens → 7%
  - 4 ou mais → 10%

- Desconto por categoria (por item):
  - CAPINHA → 3%
  - CARREGADOR → 5%
  - FONE → 3%
  - PELÍCULA → 2%
  - SUPORTE → 2%

- Limite máximo de desconto: 25%

- Cálculo final:
  - valor total de desconto
  - valor final do carrinho

---

## 6. Testes de Validação
Os testes unitários foram executados com sucesso.

- Total de testes: 8
- Testes do `CalcularCarrinhoUseCase`: 6

Os testes cobrem os cenários definidos em `CENARIOTESTE.md`, garantindo a validação da lógica implementada.

## Regras que precisam ser implementadas

### 1. Desconto por quantidade total de itens
- 1 item = 0%
- 2 itens = 5%
- 3 itens = 7%
- 4 ou mais itens = 10%

### 2. Desconto adicional por categoria
- CAPINHA = 3%
- CARREGADOR = 5%
- FONE = 3%
- PELICULA = 2%
- SUPORTE = 2%

**Importante:** O desconto de categoria é aplicado **por item**, não por categoria única. Se o carrinho tiver 3 capinhas, o desconto de categoria será 3% + 3% + 3% = 9%.

### 3. Regra de desconto máximo
- A soma dos descontos é cumulativa
- O percentual total não pode ultrapassar 25%

### 4. Total final
- valorDesconto = subtotal * percentualDesconto / 100
- total = subtotal - valorDesconto

## Observações
- Os produtos já estão carregados em memória
- A interface web existe apenas para apoio visual
- A principal validação da atividade ocorre pelos testes unitários

## Resultado esperado
Ao final da implementação:
- todos os testes devem passar, inclusive os testes de ponta a ponta
- Os cenarios de testes ponta a ponta estão descritos no arquivo `CENARIOSTESTES.md` e devem ser seguidos à risca para validação da implementação
- o resumo do carrinho deve apresentar subtotal, desconto e total final corretamente
