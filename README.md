# Comparador AIG

[![Build Status](https://travis-ci.com/FranciscoKnebel/comparadorAig.svg?token=XTCGVcTeCasm1L7c4fss&branch=master)](https://travis-ci.com/FranciscoKnebel/comparadorAig)

Implementação de dois comparadores de AAG para verificar equivalência.
A aplicação converte o arquivo AAG para uma estrutura [AIG](https://en.wikipedia.org/wiki/And-inverter_graph) e depois converte para uso em um dos comparadores disponíveis.

## Building
#### Make
#### Make aag
#### Make bdd
#### Make test
#### Make clean

## Using
	TODO

## Arquivo de entrada AAG
	TODO

## Comparando utilizando BDDs
Para o comparador utilizando BDD, o AIG dos dois arquivos de entrada é convertido em duas expressões lógicas, salvas em um arquivo e lido pelo comparador **bdd-cmp**.

## Comparando utilizando SAT
	TODO

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
