# Guia Completo de Programação Funcional em Java: De Expressões Lambda a Pipelines de Streams

## Parte I: A Mudança Funcional: Compreendendo as Expressões Lambda

A introdução de expressões lambda no Java 8 marcou uma das mais significativas evoluções da linguagem, inaugurando uma era de programação funcional que permite aos desenvolvedores escrever código mais expressivo, conciso e eficiente. Esta seção desmistifica as expressões lambda, detalhando sua sintaxe, o papel fundamental das interfaces funcionais e as profundas diferenças que as separam de suas predecessoras, as classes anônimas.

### 1.1 Uma Nova Sintaxe para Comportamento: O "Porquê" das Lambdas

Antes do Java 8, sempre que era necessário passar um bloco de código como argumento para um método—uma prática comum em listeners de eventos, threads ou estratégias de ordenação—a solução padrão era o uso de classes anônimas. Embora funcional, essa abordagem era notória por sua verbosidade, obscurecendo a lógica de negócios com uma sintaxe excessiva.[1, 2] O objetivo principal das expressões lambda é resolver exatamente esse problema: fornecer uma maneira clara e compacta de representar uma função anônima, permitindo que o comportamento seja tratado como um argumento de método, ou "código como dados".[1, 3]

Para ilustrar essa transformação, considere a tarefa comum de ordenar uma lista de objetos `Pessoa` pelo nome.

**A Abordagem Tradicional (Pré-Java 8) com Classe Anônima:**
A implementação de um `Comparator` exigia a criação de uma instância de uma classe anônima, completa com a declaração do método `compare` e a anotação `@Override`.

```java
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//... definição da classe Pessoa com getNome()

List\<Pessoa\> listaPessoas =...;

Collections.sort(listaPessoas, new Comparator\<Pessoa\>() {
@Override
public int compare(Pessoa p1, Pessoa p2) {
return p1.getNome().compareTo(p2.getNome());
}
});
```

O código acima é funcional, mas a lógica essencial—`p1.getNome().compareTo(p2.getNome())`—está envolta em cinco linhas de sintaxe boilerplate.

**A Abordagem Moderna (Java 8+) com Expressão Lambda:**
Com uma expressão lambda, a mesma operação de ordenação é expressa de forma drasticamente mais limpa e direta.[5, 6]

```java
import java.util.Collections;
import java.util.List;

//... definição da classe Pessoa com getNome()

List<Pessoa> listaPessoas =...;

Collections.sort(listaPessoas, (Pessoa p1, Pessoa p2) -> p1.getNome().compareTo(p2.getNome()));
```

Essa comparação lado a lado revela o poder das lambdas: elas eliminam o ruído sintático e permitem que o desenvolvedor se concentre no *o quê* (a lógica de comparação) em vez do *como* (a cerimônia de criação de um objeto).[2, 4] Essa mudança não é apenas estética; ela promove um estilo de programação mais declarativo e legível, que é a base da API de Streams.

### 1.2 A Anatomia de uma Expressão Lambda

Uma expressão lambda possui uma estrutura simples e consistente, composta por três partes principais [1, 5]:

1.  **Lista de Parâmetros:** Uma lista de parâmetros de entrada, entre parênteses. Os tipos dos parâmetros podem ser declarados explicitamente ou, na maioria dos casos, inferidos pelo compilador.
2.  **Seta (Arrow Token):** O token `->` separa a lista de parâmetros do corpo da expressão.
3.  **Corpo (Body):** Uma única expressão ou um bloco de código que define a lógica da função.

A sintaxe é flexível e se adapta a diferentes cenários, tornando o código ainda mais conciso.

  * **Zero Parâmetros:** Quando a função não requer parâmetros, parênteses vazios `()` são utilizados. Isso é comum ao implementar a interface `Runnable`.

    ```java
    Runnable r = () -> System.out.println("Executando em uma nova thread");
    new Thread(r).start();
    ```

  * **Um Parâmetro:** Se houver apenas um parâmetro e seu tipo for inferido, os parênteses são opcionais.[1, 8]

    ```java
    // Com parênteses e tipo explícito
    Consumer<String> impressora1 = (String s) -> System.out.println(s);

    // Com tipo inferido
    Consumer<String> impressora2 = (s) -> System.out.println(s);

    // Sem parênteses (mais comum)
    Consumer<String> impressora3 = s -> System.out.println(s);
    ```

  * **Múltiplos Parâmetros:** Para dois ou mais parâmetros, os parênteses são obrigatórios.

    ```java
    // Tipos explícitos
    BinaryOperator<Integer> soma1 = (Integer a, Integer b) -> a + b;

    // Tipos inferidos (mais comum)
    BinaryOperator<Integer> soma2 = (a, b) -> a + b;
    ```

  * **Corpo com Expressão Única:** Se o corpo contiver apenas uma expressão, o valor dessa expressão é retornado implicitamente. As chaves `{}` e a instrução `return` não são necessárias.[1]

    ```java
    // Retorna implicitamente a soma de a e b
    (a, b) -> a + b;
    ```

  * **Corpo com Bloco de Código:** Para múltiplas instruções, o corpo deve ser delimitado por chaves `{}`. Se um valor de retorno for esperado, a instrução `return` deve ser usada explicitamente.[1, 7]

    ```java
    (String s) -> {
        System.out.println("Processando a string: " + s);
        return s.toUpperCase();
    }
    ```

A tabela a seguir resume essas variações para referência rápida.

| Cenário | Sintaxe | Exemplo |
| :--- | :--- | :--- |
| Zero Parâmetros | `() -> corpo` | `() -> "Olá, Mundo!"` |
| Um Parâmetro (tipo inferido) | `param -> corpo` | `s -> s.length()` |
| Múltiplos Parâmetros | `(p1, p2) -> corpo` | `(a, b) -> a + b` |
| Corpo de Expressão Única | `(params) -> expressao` | `(x, y) -> x * y` |
| Corpo de Bloco de Código | `(params) -> { instrucoes; }` | `(n) -> { if (n > 0) return "Positivo"; else return "Não positivo"; }` |

### 1.3 Interfaces Funcionais: O Alvo das Lambdas

Uma expressão lambda, por si só, não possui um tipo intrínseco. Seu tipo é determinado pelo contexto em que é utilizada, um conceito conhecido como "tipagem por alvo" (target typing). O alvo de uma expressão lambda deve ser uma **interface funcional**.

Uma interface funcional é definida por uma regra simples: ela deve conter **exatamente um método abstrato** (conhecida como SAM, de Single Abstract Method). É essa restrição que permite ao compilador Java associar inequivocamente o corpo da lambda àquele único método abstrato. Uma interface funcional pode conter múltiplos métodos `default` ou `static`, pois eles já possuem uma implementação e não contam para o limite de um método abstrato.

Para garantir que uma interface mantenha o contrato SAM, é uma boa prática anotá-la com `@FunctionalInterface`. Essa anotação instrui o compilador a gerar um erro se mais de um método abstrato for adicionado, prevenindo modificações acidentais que quebrariam sua funcionalidade com lambdas.

O Java 8 introduziu o pacote `java.util.function`, que fornece um conjunto robusto de interfaces funcionais genéricas para cobrir os casos de uso mais comuns. Compreender as quatro categorias principais é essencial para dominar a API de Streams.

| Interface Funcional | Método Abstrato | Descrição | Caso de Uso Comum |
| :--- | :--- | :--- | :--- |
| `Predicate<T>` | `boolean test(T t)` | Recebe um argumento e retorna um valor booleano. | `Stream.filter()` |
| `Function<T, R>` | `R apply(T t)` | Recebe um argumento do tipo `T` e retorna um resultado do tipo `R`. | `Stream.map()` |
| `Consumer<T>` | `void accept(T t)` | Recebe um argumento e realiza uma ação (efeito colateral), sem retornar nada. | `Stream.forEach()` |
| `Supplier<T>` | `T get()` | Não recebe argumentos e produz (fornece) um resultado. | `Stream.generate()` |

Essas interfaces, juntamente com suas especializações para tipos primitivos (`IntPredicate`, `DoubleFunction`, etc.) e variações de aridade (`BiFunction`, `BiConsumer`), formam a espinha dorsal da programação funcional em Java, servindo como os blocos de construção para pipelines de dados complexos.

### 1.4 Análise de Especialista: Lambdas vs. Classes Anônimas

Para o programador iniciante, as expressões lambda podem parecer meramente "açúcar sintático" sobre as classes anônimas. No entanto, essa visão é superficial. As diferenças são profundas, estendendo-se à semântica, ao escopo e, crucialmente, à implementação no nível da JVM, com implicações diretas no desempenho.

1.  **Escopo do `this`:** Esta é uma das distinções mais importantes.

      * **Classe Anônima:** A palavra-chave `this` refere-se à própria instância da classe anônima. Ela cria um novo escopo de `this`.[16]
      * **Expressão Lambda:** `this` tem escopo léxico, o que significa que se refere à instância da classe que a envolve (enclosing class). A lambda não introduz um novo escopo para `this`.[16, 17] Essa característica torna as lambdas mais intuitivas em muitos cenários, especialmente em listeners de UI, onde a intenção é frequentemente interagir com os campos da classe principal.

2.  **Shadowing de Variáveis:**

      * **Classe Anônima:** Por introduzir um novo escopo, uma classe anônima pode declarar variáveis (incluindo parâmetros de método) com o mesmo nome de variáveis na classe externa, "sombreando-as" (shadowing).[6]
      * **Expressão Lambda:** Não introduz um novo escopo. Portanto, é um erro de compilação declarar um parâmetro ou variável local em uma lambda com o mesmo nome de uma variável no escopo que a envolve.[6]

3.  **Estado e Campos:**

      * **Classe Anônima:** Pode ter seu próprio estado, ou seja, declarar campos (variáveis de instância).[6, 17]
      * **Expressão Lambda:** É conceitualmente sem estado (stateless). Ela não pode declarar variáveis de instância. As variáveis que ela declara dentro de seu corpo são locais à sua execução.[16, 17]

4.  **Tipos de Implementação:**

      * **Classe Anônima:** É mais flexível. Pode implementar qualquer interface (mesmo aquelas com múltiplos métodos abstratos) ou estender uma classe (abstrata ou concreta).[6, 17]
      * **Expressão Lambda:** Só pode ser usada para implementar interfaces funcionais (aquelas com um único método abstrato).[6, 17]

5.  **Compilação e Desempenho:** Esta é a diferença mais profunda e impactante.

      * **Classe Anônima:** Para cada classe anônima definida no código-fonte, o compilador Java (`javac`) gera um arquivo `.class` separado (por exemplo, `MinhaClasse$1.class`). Essa proliferação de arquivos de classe aumenta o tamanho do artefato de implantação (arquivo JAR), e cada uma dessas classes precisa ser carregada e verificada pela JVM em tempo de execução, o que adiciona uma sobrecarga ao tempo de inicialização da aplicação e consome mais memória no Metaspace.[2, 16, 17]
      * **Expressão Lambda:** A implementação das lambdas foi uma decisão de design estratégica para evitar as desvantagens das classes anônimas. Em vez de gerar um arquivo `.class` para cada lambda, o compilador adota uma abordagem diferente. Ele gera um método `private static` na própria classe que contém a lambda, e o corpo desse método é o corpo da lambda. Em seguida, ele insere no bytecode a instrução `invokedynamic`, que foi introduzida no Java 7. Essa instrução adia a estratégia de vinculação da lambda para o tempo de execução. Somente na primeira vez que o código é executado, a JVM cria dinamicamente um objeto que implementa a interface funcional e o vincula à chamada. Essa abordagem é significativamente mais eficiente: reduz o tamanho do JAR, diminui o tempo de carregamento de classes e permite que a JVM aplique otimizações futuras sem a necessidade de recompilar o código.[6, 16]

A escolha de usar `invokedynamic` para lambdas representa uma mudança fundamental na forma como a JVM lida com o comportamento dinâmico. A sobrecarga de criar classes anônimas era um impedimento conhecido para a adoção de estilos de programação mais funcionais. Ao aproveitar `invokedynamic`, os arquitetos da linguagem Java forneceram um mecanismo leve e de alto desempenho, tornando as lambdas uma ferramenta de primeira classe que não apenas melhora a legibilidade do código, mas também otimiza sua execução.

A tabela a seguir consolida essas diferenças cruciais.

| Característica | Classe Anônima | Expressão Lambda |
| :--- | :--- | :--- |
| **Sintaxe** | Verbosa, com boilerplate | Concisa e expressiva |
| **Escopo do `this`** | Refere-se à própria instância da classe anônima | Refere-se à instância da classe que a envolve (escopo léxico) |
| **Shadowing** | Pode sombrear variáveis do escopo externo | Não pode sombrear variáveis do escopo externo |
| **Estado (Campos)** | Pode ter variáveis de instância | Não pode ter variáveis de instância (é sem estado) |
| **Tipo de Implementação** | Pode estender classes ou implementar interfaces com múltiplos métodos | Só pode implementar interfaces funcionais (SAM) |
| **Compilação** | Gera um arquivo `.class` separado para cada instância | Não gera arquivos `.class` extras; usa a instrução `invokedynamic` |
| **Desempenho** | Maior sobrecarga de carregamento de classes e uso de memória | Geralmente mais eficiente devido à vinculação tardia e otimizações da JVM |

## Parte II: Processamento Declarativo de Dados com a API de Streams

A API de Streams, introduzida no Java 8, é uma abstração poderosa para processar sequências de dados de forma declarativa. Ela permite que os desenvolvedores expressem consultas de processamento de dados complexas, como filtragem, mapeamento e redução, de maneira concisa e legível, aproveitando a arquitetura multi-core moderna sem a necessidade de escrever código de threading explícito.[18]

### 2.1 Introduzindo o Pipeline de Streams

Um stream não é uma estrutura de dados que armazena elementos; em vez disso, é uma sequência de elementos de uma fonte que suporta operações de agregação.[18, 19] Pense em um stream como uma linha de montagem em uma fábrica: os materiais brutos (os dados da fonte) entram em uma extremidade, passam por várias estações (operações) que os transformam e, no final, um produto acabado (o resultado) emerge.[20]

As características fundamentais de um stream são:

  * **Não armazena dados:** Um stream transporta valores de uma fonte (como uma `Collection` ou um array) através de um pipeline de operações computacionais. Ele não armazena os elementos em si.[18, 19]
  * **Funcional na natureza:** Uma operação em um stream produz um resultado, mas não modifica sua fonte de dados original. Por exemplo, filtrar um stream obtido de uma coleção cria um novo stream com os elementos filtrados, em vez de remover elementos da coleção de origem.[21]
  * **Avaliação Preguiçosa (Lazy Evaluation):** Muitas operações de stream (as intermediárias) são preguiçosas. Isso significa que a computação sobre os elementos da fonte só ocorre quando uma operação terminal é iniciada. Essa característica permite otimizações significativas.[19, 21]
  * **Possivelmente Ilimitado:** Como os elementos são computados sob demanda, um stream pode representar uma sequência infinita, como a sequência de todos os números pares ou uma série de números aleatórios.[18]

A estrutura de processamento de um stream é chamada de **pipeline de stream** e consiste em três partes distintas [18, 19, 20, 21]:

1.  **Fonte (Source):** O ponto de partida do pipeline, que fornece a sequência de elementos. Pode ser uma coleção, um array, uma função geradora ou um canal de E/S.
2.  **Operações Intermediárias (Intermediate Operations):** Uma cadeia de zero ou mais operações que transformam o stream. Cada operação intermediária retorna um novo stream, permitindo que as operações sejam encadeadas. Exemplos incluem `filter()` e `map()`.
3.  **Operação Terminal (Terminal Operation):** Uma operação final que inicia a execução de todo o pipeline e produz um resultado ou um efeito colateral. Exemplos incluem `collect()`, `forEach()` ou `sum()`. Após a execução de uma operação terminal, o stream é considerado "consumido" e não pode ser reutilizado.

### 2.2 Criação de Streams e Fontes de Dados

A API de Streams oferece várias maneiras convenientes de criar um stream a partir de diversas fontes de dados.

  * **A partir de uma Coleção:** A forma mais comum de criar um stream é chamar o método `stream()` em qualquer objeto que implemente a interface `Collection`.[19, 22, 23]

    ```java
    List<String> nomes = Arrays.asList("Ana", "Bruno", "Carla");
    Stream<String> streamDeNomes = nomes.stream();
    ```

  * **A partir de um Array:** A classe `Arrays` fornece o método estático `stream()` para criar um stream a partir de um array.[18, 19, 24]

    ```java
    String nomesArray = {"Ana", "Bruno", "Carla"};
    Stream<String> streamDeArray = Arrays.stream(nomesArray);
    ```

  * **A partir de Valores Estáticos:** O método `Stream.of()` permite criar um stream a partir de um número variável de elementos individuais.[18, 19]

    ```java
    Stream<String> streamDeValores = Stream.of("Ana", "Bruno", "Carla");
    ```

  * **Streams Infinitos:** A API fornece dois métodos para gerar streams que, teoricamente, não têm fim. Eles são frequentemente usados em conjunto com a operação `limit()` para criar uma sequência finita.

      * `Stream.iterate()`: Cria um stream sequencial e ordenado, aplicando uma função repetidamente ao elemento anterior. O primeiro argumento é a semente (valor inicial).[18, 24]
        ```java
        // Gera os 10 primeiros números pares a partir de 0
        Stream<Integer> streamDePares = Stream.iterate(0, n -> n + 2).limit(10);
        ```
      * `Stream.generate()`: Cria um stream onde cada elemento é gerado por um `Supplier`. É útil para criar streams de valores constantes ou aleatórios.[18]
        ```java
        // Gera 5 números aleatórios
        Stream<Double> streamAleatorio = Stream.generate(Math::random).limit(5);
        ```

  * **A partir de Arquivos e Strings:**

      * É possível criar um stream de linhas de um arquivo de texto usando `Files.lines()`.[19, 23]
        ```java
        try (Stream<String> linhas = Files.lines(Paths.get("meu-arquivo.txt"))) {
            linhas.forEach(System.out::println);
        }
        ```
      * A classe `String` possui o método `chars()`, que retorna um `IntStream` representando os caracteres da string.[24, 25]
        ```java
        IntStream streamDeChars = "Java".chars(); // Retorna um IntStream com os valores ASCII
        ```

### 2.3 Operações Intermediárias: Transformações Preguiçosas e Componíveis

As operações intermediárias são o coração do processamento de streams. Elas são sempre **preguiçosas** (lazy), o que significa que não executam nenhuma ação até que uma operação terminal seja invocada. Cada operação intermediária retorna um novo stream, o que permite encadeá-las para formar um pipeline de processamento sofisticado.[21, 26, 27]

As operações intermediárias mais comuns incluem:

  * `filter(Predicate<T> predicate)`: Retorna um stream contendo apenas os elementos que satisfazem a condição do `Predicate` fornecido.[18, 28]
  * `map(Function<T, R> mapper)`: Transforma cada elemento do stream em outro objeto, aplicando a função `mapper`. O tipo dos elementos pode mudar.[22, 29]
  * `flatMap(Function<T, Stream<R>> mapper)`: Semelhante ao `map`, mas a função de mapeamento deve retornar um stream. O `flatMap` então "achata" todos os streams gerados em um único stream de saída. É útil para lidar com estruturas aninhadas, como uma lista de listas.[28, 30]
  * `distinct()`: Retorna um stream com os elementos duplicados removidos, com base na implementação do método `equals()`.[18, 19]
  * `sorted()`: Retorna um stream com os elementos ordenados de acordo com sua ordem natural ou um `Comparator` fornecido. Esta é uma operação *stateful*, pois pode precisar ver todos os elementos antes de produzir um resultado.[19, 21, 28]
  * `limit(long maxSize)`: Trunca o stream para não ter mais do que `maxSize` elementos. É uma operação de *curto-circuito* (short-circuiting), pois pode parar de processar elementos assim que o limite for atingido.[18, 21]
  * `skip(long n)`: Descarta os primeiros `n` elementos do stream.[18]
  * `peek(Consumer<T> action)`: Executa uma ação em cada elemento à medida que ele passa pelo pipeline. É usado principalmente para depuração, pois permite "espiar" os elementos em um determinado ponto do pipeline sem modificá-los.[28, 31]

A característica mais poderosa das operações intermediárias é a **avaliação preguiçosa** e a **fusão de loops (loop fusion)**. Um equívoco comum é pensar que `list.stream().filter(...).map(...)` primeiro itera sobre toda a lista para filtrar e cria uma coleção intermediária, e depois itera sobre essa nova coleção para mapear. Na realidade, o mecanismo é muito mais eficiente. As operações intermediárias constroem uma "receita" de processamento que só é executada quando a operação terminal é chamada.[32] Nesse momento, o stream puxa o primeiro elemento da fonte e o passa por todo o pipeline (`filter`, depois `map`). Em seguida, puxa o segundo elemento e faz o mesmo. As operações são fundidas em uma única passagem sobre os dados, evitando a criação de coleções intermediárias e reduzindo significativamente o uso de memória.[31, 32]

O exemplo a seguir, usando `peek`, demonstra esse fluxo:

```java
List<String> nomes = Arrays.asList("Ana", "Bruno", "Carla", "Bia");

nomes.stream()
   .filter(s -> {
         System.out.println("Filtrando: " + s);
         return s.startsWith("B");
     })
   .map(s -> {
         System.out.println("Mapeando: " + s);
         return s.toUpperCase();
     })
   .forEach(s -> System.out.println("Resultado: " + s));

// Saída:
// Filtrando: Ana
// Filtrando: Bruno
// Mapeando: Bruno
// Resultado: BRUNO
// Filtrando: Carla
// Filtrando: Bia
// Mapeando: Bia
// Resultado: BIA
```

A saída mostra claramente que cada elemento passa por todo o pipeline individualmente, em vez de as operações serem aplicadas a toda a coleção de uma vez.

### 2.4 Operações Terminais: Acionando a Execução e Produzindo Resultados

As operações terminais são as que iniciam o processamento de um pipeline de stream. Elas são, na maioria dos casos, **ansiosas** (eager), o que significa que consomem o stream para produzir um resultado final ou um efeito colateral.[21, 27] Uma vez que uma operação terminal é chamada, o stream é fechado e não pode ser reutilizado.[26]

As operações terminais podem ser agrupadas por seu tipo de resultado:

  * **Efeitos Colaterais:**

      * `forEach(Consumer<T> action)`: Executa uma ação para cada elemento do stream. Frequentemente usada para imprimir elementos ou interagir com sistemas externos.[22, 33]

  * **Coleta em Coleções:**

      * `collect(Collector collector)`: A operação terminal mais versátil. Usa um `Collector` para acumular os elementos do stream em um contêiner, como uma `List`, `Set` ou `Map`.[22, 33]
        ```java
        List<String> nomesMaiusculos = streamDeNomes.map(String::toUpperCase)
                                                  .collect(Collectors.toList());
        ```
      * `toArray()`: Coleta os elementos do stream em um array.[22]

  * **Redução para um Valor Único:**

      * `count()`: Retorna o número de elementos no stream como um `long`.[19, 33]
      * `reduce()`: Combina os elementos do stream para produzir um único valor, como a soma ou o máximo.[19, 33]
      * `min(Comparator)` e `max(Comparator)`: Retornam o elemento mínimo ou máximo, respectivamente, como um `Optional`.[33]

  * **Verificações Condicionais (Short-Circuiting):**

      * `anyMatch(Predicate)`: Retorna `true` se pelo menos um elemento corresponder ao predicado.
      * `allMatch(Predicate)`: Retorna `true` se todos os elementos corresponderem ao predicado.
      * `noneMatch(Predicate)`: Retorna `true` se nenhum elemento corresponder ao predicado.
        Essas operações podem terminar o processamento antecipadamente assim que o resultado for determinado.[33, 34]

  * **Busca de Elementos:**

      * `findFirst()`: Retorna um `Optional` descrevendo o primeiro elemento do stream.
      * `findAny()`: Retorna um `Optional` descrevendo qualquer elemento do stream. É particularmente útil em streams paralelos, onde pode ser mais eficiente do que `findFirst`.[19, 33]

A tabela a seguir serve como um guia de referência rápida para as operações de stream mais comuns.

| Operações Intermediárias (Retornam `Stream`) | Operações Terminais (Produzem Resultado) |
| :--- | :--- |
| `filter(predicate)` - Filtra elementos | `forEach(action)` - Executa uma ação por elemento |
| `map(mapper)` - Transforma elementos | `collect(collector)` - Coleta em uma coleção/mapa |
| `flatMap(mapper)` - Transforma e achata streams | `reduce(identity, accumulator)` - Reduz a um valor único |
| `distinct()` - Remove duplicatas | `count()` - Conta os elementos |
| `sorted()` - Ordena elementos | `anyMatch()`, `allMatch()`, `noneMatch()` - Verificações booleanas |
| `limit(n)` - Trunca o stream | `findFirst()`, `findAny()` - Encontra um elemento |
| `skip(n)` - Pula elementos | `min(comparator)`, `max(comparator)` - Encontra min/max |
| `peek(action)` - Executa ação para depuração | `toArray()` - Converte para array |

## Parte III: Processamento de Alta Performance com Streams Primitivos: O `IntStream`

Embora a API de Streams genérica (`Stream<T>`) seja poderosa, ela tem uma limitação de desempenho quando se trata de tipos primitivos como `int`, `long` e `double`. Para resolver isso, a biblioteca padrão do Java fornece especializações de stream para esses tipos: `IntStream`, `LongStream` e `DoubleStream`. Esta seção foca no `IntStream`, explicando por que ele existe e como usá-lo para escrever código numérico eficiente.

### 3.1 A Necessidade de Performance: `IntStream` vs. `Stream<Integer>`

A principal razão para a existência do `IntStream` é o desempenho. A diferença fundamental entre `IntStream` e `Stream<Integer>` reside nos conceitos de **boxing** e **unboxing**.[35]

  * **Boxing (Empacotamento):** É a conversão automática de um valor de tipo primitivo (como `int`) para seu objeto wrapper correspondente (`Integer`).
  * **Unboxing (Desempacotamento):** É o processo inverso, de `Integer` para `int`.

Um `Stream<Integer>` é um stream de objetos `Integer`. Cada `Integer` é um objeto completo na heap da JVM, o que acarreta uma sobrecarga de memória (para o cabeçalho do objeto e outros metadados) em comparação com um `int` primitivo. Quando você realiza operações aritméticas em um `Stream<Integer>`, como somar seus elementos, a JVM precisa desempacotar cada objeto `Integer` para um `int` primitivo, realizar a operação e, em alguns casos, empacotar o resultado de volta em um `Integer`. Para grandes volumes de dados, esse ciclo contínuo de boxing e unboxing gera uma pressão significativa na CPU e no coletor de lixo (Garbage Collector), degradando o desempenho.[22, 35]

O `IntStream`, por outro lado, é um stream especializado que opera diretamente sobre valores `int` primitivos. Ele evita completamente o overhead de boxing/unboxing, tornando as operações numéricas muito mais rápidas e eficientes em termos de memória.[35, 36, 37]

A criação de streams primitivos não foi uma mera conveniência, mas uma decisão de design pragmática e crucial. O sistema de genéricos do Java não suporta tipos primitivos, o que significa que não é possível ter uma `List<int>`. Consequentemente, a API genérica `Stream<T>` é forçada a usar `Stream<Integer>`. Reconhecendo que o processamento numérico é um caso de uso extremamente comum e sensível ao desempenho, os arquitetos da linguagem forneceram um caminho otimizado através de streams especializados. A lição para os desenvolvedores é clara: sempre que o desempenho com números for uma prioridade, `IntStream`, `LongStream` ou `DoubleStream` devem ser a escolha padrão.

### 3.2 Guia Prático do `IntStream`

O `IntStream` oferece um conjunto de métodos otimizados para trabalhar com inteiros.

**Criando um `IntStream`:**
Além dos métodos `of()` e `generate()`/`iterate()` compartilhados com `Stream<T>`, o `IntStream` possui métodos de fábrica estáticos muito úteis para criar sequências de números [38]:

  * `IntStream.range(int startInclusive, int endExclusive)`: Cria um stream de inteiros sequenciais começando em `startInclusive` até, mas não incluindo, `endExclusive`.[36, 39, 40]

    ```java
    // Gera um IntStream de 1, 2, 3, 4
    IntStream.range(1, 5).forEach(System.out::println);
    ```

  * `IntStream.rangeClosed(int startInclusive, int endInclusive)`: Semelhante ao `range`, mas inclui o valor final `endInclusive`.[37, 40]

    ```java
    // Gera um IntStream de 1, 2, 3, 4, 5
    IntStream.rangeClosed(1, 5).forEach(System.out::println);
    ```

  * `Arrays.stream(int array)`: Cria um `IntStream` a partir de um array de `int` primitivos.[37]

    ```java
    int numeros = {1, 2, 3, 4, 5};
    IntStream streamDeArray = Arrays.stream(numeros);
    ```

**Convertendo entre Tipos de Stream:**
É comum a necessidade de converter entre streams de objetos e streams primitivos.

  * **De `Stream<Integer>` para `IntStream`:** Use o método de mapeamento `mapToInt()`. Ele recebe uma função que extrai um `int` de cada objeto `Integer`.[41, 42]

    ```java
    List<Integer> lista = Arrays.asList(1, 2, 3);
    IntStream intStream = lista.stream().mapToInt(Integer::intValue);
    ```

  * **De `IntStream` para `Stream<Integer>`:** Use o método `boxed()`, que empacota cada `int` primitivo em um objeto `Integer`.[36, 38, 43]

    ```java
    IntStream intStream = IntStream.range(1, 4); // 1, 2, 3
    Stream<Integer> streamDeInteger = intStream.boxed();
    ```

O `IntStream` também oferece operações terminais especializadas e otimizadas, como `sum()`, `average()`, e `summaryStatistics()`, que calculam a soma, a média, a contagem, o mínimo e o máximo em uma única passagem.[38, 43]

### 3.3 `IntStream` em Ação: Um Pipeline Completo

Para consolidar todos os conceitos abordados, vamos construir um pipeline de `IntStream` do início ao fim para resolver um problema prático.

**Problema:** Dada uma lista de números inteiros, calcule a soma dos quadrados de todos os números que são pares e únicos.

Vamos começar com uma lista de números que contém duplicatas e valores ímpares e pares.

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class ExemploIntStream {
    public static void main(String args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 2, 4, 5, 4, 6, 7, 8);
```

Agora, construiremos o pipeline passo a passo:

1.  **Fonte e Criação do Stream:** Começamos criando um `Stream<Integer>` a partir da lista.

    ```java
    numeros.stream() // Retorna um Stream<Integer>
    ```

2.  **Conversão para Primitivo:** Para otimizar o desempenho, convertemos para um `IntStream` imediatamente.

    ```java
    ```

.mapToInt(Integer::intValue) // Retorna um IntStream
\`\`\`

3.  **Remover Duplicatas (Operação Intermediária):** Usamos `distinct()` para garantir que cada número seja processado apenas uma vez. O stream agora contém `1, 2, 3, 4, 5, 6, 7, 8`.[44, 45]
    ```java
    ```

.distinct() // Retorna um IntStream
\`\`\`

4.  **Filtrar Números Pares (Operação Intermediária):** Usamos `filter()` para manter apenas os números pares. O stream agora contém `2, 4, 6, 8`.[38, 46]
    ```java
    ```

.filter(n -\> n % 2 == 0) // Retorna um IntStream
\`\`\`

5.  **Mapear para Quadrados (Operação Intermediária):** Usamos `map()` para transformar cada número par em seu quadrado. O stream agora contém `4, 16, 36, 64`.[38, 43]
    ```java
    ```

.map(n -\> n \* n) // Retorna um IntStream
\`\`\`

6.  **Calcular a Soma (Operação Terminal):** Finalmente, usamos a operação terminal `sum()` para calcular o resultado final.[47]
    ```java
    ```

.sum(); // Retorna um int
\`\`\`

Juntando tudo, o pipeline completo é uma única instrução, elegante e altamente legível, que encapsula toda a lógica de processamento de dados.[38, 48]

```java
// Código completo
import java.util.Arrays;
import java.util.List;

public class ExemploIntStreamCompleto {
    public static void main(String args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 2, 4, 5, 4, 6, 7, 8);

        int somaDosQuadradosDosParesUnicos = numeros.stream()      // 1. Cria Stream<Integer>
              .mapToInt(Integer::intValue)                       // 2. Converte para IntStream
              .distinct()                                        // 3. Remove duplicatas -> 1, 2, 3, 4, 5, 6, 7, 8
              .filter(n -> n % 2 == 0)                           // 4. Filtra pares -> 2, 4, 6, 8
              .map(n -> n * n)                                   // 5. Mapeia para quadrados -> 4, 16, 36, 64
              .sum();                                            // 6. Soma os elementos -> 120

        System.out.println("A soma dos quadrados dos números pares únicos é: " + somaDosQuadradosDosParesUnicos);
        // Saída: A soma dos quadrados dos números pares únicos é: 120
    }
}
```

Este exemplo demonstra a sinergia de todos os conceitos: a criação de um stream, o encadeamento de operações intermediárias preguiçosas e a execução final por uma operação terminal, tudo isso enquanto aproveita a eficiência de um `IntStream`.

## Conclusão: Melhores Práticas para um Java Funcional e Moderno

A introdução das expressões lambda e da API de Streams no Java 8 representou mais do que uma simples adição de recursos; foi uma mudança de paradigma que equipou os desenvolvedores com ferramentas para escrever um código mais declarativo, conciso e, em muitos casos, mais performático. Ao internalizar os conceitos e as melhores práticas discutidos neste guia, os desenvolvedores podem aproveitar ao máximo essas poderosas funcionalidades.

As principais recomendações para escrever um código Java moderno e funcional são:

  * **Prefira Lambdas a Classes Anônimas:** Para a implementação de interfaces funcionais, as expressões lambda devem ser a escolha padrão. Elas oferecem uma sintaxe mais limpa, reduzem o código boilerplate e se beneficiam de uma estratégia de compilação mais eficiente através do `invokedynamic`, resultando em melhor desempenho em tempo de execução.
  * **Domine as Interfaces Funcionais Padrão:** Familiarize-se com as interfaces do pacote `java.util.function`, especialmente `Predicate`, `Function`, `Consumer` e `Supplier`. Elas são os blocos de construção fundamentais da API de Streams e aparecem constantemente em código funcional.
  * **Estruture o Processamento de Dados como Pipelines:** Adote a mentalidade de pipeline (fonte -\> operações intermediárias -\> operação terminal) para processar coleções. Essa abordagem declarativa torna a intenção do código mais clara e aproveita a avaliação preguiçosa para otimizações automáticas.
  * **Utilize Streams Primitivos para Desempenho:** Ao trabalhar com grandes volumes de dados numéricos (`int`, `long`, `double`), use sempre as especializações de stream (`IntStream`, `LongStream`, `DoubleStream`). Evitar o overhead de boxing e unboxing é uma das otimizações de desempenho mais importantes que a API de Streams oferece.
  * **Priorize Funções Puras e Sem Efeitos Colaterais:** Nas operações intermediárias (como `filter` e `map`), esforce-se para usar lambdas que sejam funções puras—ou seja, seu resultado depende apenas de suas entradas e elas não têm efeitos colaterais observáveis. Isso torna o pipeline mais previsível, mais fácil de testar e seguro para paralelização.

Ao seguir essas diretrizes, os desenvolvedores podem escrever um código que não é apenas mais fácil de ler e manter, mas que também está alinhado com a evolução da plataforma Java em direção a um modelo de programação mais flexível e poderoso.

