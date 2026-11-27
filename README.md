# Sistema de Abrigos e Resgates de Animais

Um sistema feito em Java, rodando no terminal, para ajudar a organizar um abrigo de animais. Com ele dá pra cadastrar os animais, acompanhar a saúde de cada um, controlar as adoções e organizar as atividades dos voluntários.

Esse projeto é o trabalho final de Programação Orientada a Objetos.

## O que dá pra fazer

- Cadastrar cachorros, gatos e outros animais, editar, remover, buscar e filtrar
- Registrar consultas, vacinas, medicamentos e cirurgias de cada animal
- Pedir, aprovar ou rejeitar adoções, e também registrar devoluções
- Criar atividades (campanhas de vacinação, eventos, feiras de adoção e mutirões) e inscrever voluntários
- Receber avisos automáticos de vacinas e eventos chegando perto
- Gerar relatórios e estatísticas

## Quem usa o sistema

Existem três tipos de usuário, cada um com o seu menu:

- **Administrador:** cuida dos animais e dos usuários, aprova adoções, cria atividades e vê os relatórios.
- **Veterinário:** registra atendimentos, vacinas e tratamentos, e atualiza a saúde dos animais.
- **Voluntário:** vê os animais disponíveis, faz pedidos de adoção e participa das atividades.

Voluntários e veterinários se cadastram direto na tela inicial. Administrador só pode ser criado por outro administrador.

**Primeiro acesso:** o sistema já cria um administrador pra você entrar:

- Login: `admin`
- Senha: `admin123`

## Algumas regras do sistema

- Animal em estado crítico não pode ser adotado.
- Animal que já foi adotado alguma vez não pode ser removido.
- Cada animal só pode ter uma adoção aprovada por vez.
- Só o administrador decide sobre adoções.
- Só o veterinário registra informações médicas.
- Só voluntário se inscreve em atividades, e não dá pra se inscrever duas vezes.
- O CPF é validado no cadastro.

## Como rodar

Você precisa ter o **JDK 11 ou mais novo** instalado.

Baixe o projeto:

```bash
git clone https://github.com/LuciusHx/gerenciamento-abrigos-resgates-animais.git
cd gerenciamento-abrigos-resgates-animais
```

Depois, dentro da pasta do projeto, compile e rode:

**Linux ou macOS**

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find . -name "*.java")
java -cp out Main
```

**Windows (PowerShell)**

```powershell
mkdir out -Force
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java).FullName
java -cp out Main
```

Se preferir, é só abrir a pasta na sua IDE e rodar a classe `Main`.

> **Importante:** os dados ficam só na memória. Quando você fecha o programa, tudo que foi cadastrado some.

## Como o projeto está organizado

```
Main.java     → onde o programa começa (menu inicial e login)
models/       → as "coisas" do sistema: animais, usuários, adoções, atividades...
servicos/     → as regras de funcionamento (adoção, saúde, alertas...)
ui/           → os menus que aparecem no terminal
excecao/      → os erros personalizados do sistema
contrato/     → as interfaces
util/         → validação de CPF e constantes
```

## O que usamos de POO

Herança e classes abstratas (`Animal` e `Atividade`), polimorfismo, interfaces, encapsulamento, composição (todo animal tem seu histórico médico), enums e exceções próprias.

## Quem fez

- Bernardo Cidrão Neves
- Lucius Hebert De Matos Machado
