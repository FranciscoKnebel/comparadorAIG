# Comparador AIG

[![Build Status](https://travis-ci.com/FranciscoKnebel/comparadorAig.svg?token=XTCGVcTeCasm1L7c4fss&branch=master)](https://travis-ci.com/FranciscoKnebel/comparadorAig)

Implementação de dois comparadores de AAG para verificar equivalência lógica.

A aplicação converte o arquivo AAG para uma estrutura [AIG](https://en.wikipedia.org/wiki/And-inverter_graph) e depois converte para uso em um dos comparadores disponíveis.

Comparador desenvolvido para a disciplina _INF01205 CAD para Sistemas Digitais_.

## Building
#### Make
Compila todos os módulos individuais, seguido pela aplicação principal.

#### Make aag
Compila o leitor de AAG para AIG.

#### Make (bdd || bdd-file || sat)
Compila o módulo do comparador escolhido (BDD ou SAT).
O comparador BDD possuí duas versões: bdd e bdd-file.
O primeiro recebe as duas expressões por argumentas, pela linha de comando.
O segundo recebe as expressões por um arquivo, informado no primeiro argumento.
Esse arquivo contém cada expressão separada por linha. Exemplo:
```
expressao1
expressao2
```

#### Make util
Compila o módulo com utilitários utilizados por outros módulos.

#### Make test
Efetua testes sobre cada um dos módulos, sendo que no documento todos testes estão definidos separadamente, permitindo apenas testes de cada módulo, individualmente.

#### Make clean
Remove arquivos temporários gerados.

## Using
```
./dst/comparador examples/aag/C17.aag examples/aag/C17-v1.aag --bdd
./dst/comparador examples/aag/C17.aag examples/aag/C17-v1.aag --sat
```

## Arquivo de entrada AAG
- 11 nodos
- 5 entradas
- 0 FFs
- 2 saidas
- 6 ANDs

Para efeitos do parsing, são lidos o arquivo na seguinte ordem:
- nodos de entradas
- nodos de saída
- nodos AND

No caso do exemplo abaixo, as entradas são 2, 4, 6, 8 e 10.
As saidas são 19 e 23 (que são os nodos 18 e 22 negados, respectivamente).
Em seguida, estão os 6 ANDs: nodo 12 é nodo 6 && nodo 2, 14 é 8 && 6 e assim em diante.

```
aag 11 5 0 2 6
2
4
6
8
10
19
23
12 6 2
14 8 6
16 15 4
18 17 13
20 15 10
22 21 17
```

## Comparando utilizando BDDs
Para o comparador utilizando BDD, o AIG dos dois arquivos de entrada é convertido em duas expressões lógicas, salvas em um arquivo e lido pelo comparador **bdd-cmp**.

## Comparando utilizando SAT
Para o comparador utilizando SAT, o mesmo processo de conversão para AIG dos dois arquivos de entrada é efetuada, mas a estrutura é convertida para o formato CNF e avaliada por um programa externo, o [Limboole](http://fmv.jku.at/limboole/). Esse programa recebe as expressões e avalia a sua **satisfiability**.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/FranciscoKnebel/comparadorAig/tags).

## Contributing
Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors
<table style="text-align: center;">
  <th>Contributors</th>
  <th>Contributions</th>
  <tr>
    <td>
      <img src="https://avatars.githubusercontent.com/FranciscoKnebel?s=75">
      <br>
      <a href="https://github.com/FranciscoKnebel">Francisco Knebel</a>
    </td>
    <td>
      <a href="https://github.com/FranciscoKnebel/comparadorAig/commits?author=FranciscoKnebel">Contributions</a> by FranciscoKnebel
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://avatars.githubusercontent.com/lzancan?s=75">
      <br>
      <a href="https://github.com/lzancan">Luciano Zancan</a>
    </td>
    <td>
      <a href="https://github.com/FranciscoKnebel/comparadorAig/commits?author=lzancan">Contributions</a> by lzancan
    </td>
  </tr>
	<tr>
		<td>
			<img src="https://avatars.githubusercontent.com/rodrigodalri?s=75">
			<br>
			<a href="https://github.com/rodrigodalri">Rodrigo Dal Ri</a>
		</td>
		<td>
			<a href="https://github.com/rodrigodalri/comparadorAig/commits?author=FranciscoKnebel">Contributions</a> by rodrigodalri
		</td>
	</tr>
</table>

See also the full list of [contributors](https://github.com/FranciscoKnebel/comparadorAig/contributors) who participated in this project.

## License
This project is licensed under the _Apache License 2.0_ - see the [LICENSE.md](LICENSE.md) file for details.
