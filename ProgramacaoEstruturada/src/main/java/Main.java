import java.util.Scanner;
import java.io.*;
import java.time.*;
import java.util.*;
import java.time.format.*;
// LINHA 125 A 231 ESTÁ COM ERRO QUE EU AINDA NÃO CONSEGUI RESOLVER
public class Main{

    static final int VISITANTE = 1;
    static final int FUNCIONARIO = 2;
    static final int ADMINISTRADOR = 3;

    static List<String[]> usuarios = new ArrayList<>(Arrays.asList(
            new String[]{"AliceBG", "adm786268", String.valueOf(ADMINISTRADOR)},
            new String[]{"FláviaAV", "vend012943", String.valueOf(FUNCIONARIO)},
            new String[]{"LarissaFF", "farma831546", String.valueOf(FUNCIONARIO)},
            new String[]{"Margarida01", "05091963", String.valueOf(VISITANTE)}
    ));

    static List<String> historico = new ArrayList<>();

    static void registrarAcesso(String usuario, String area, boolean autorizado){
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String resultado = autorizado ? "AUORIZADO" : "NEGADO";
        historico.add("[" + dataHora + "] Usuário: " + usuario + "  |  Área: " + area +  "| " + resultado);
    }

    static boolean temPermissao(String usuario, int nivelUsuario, String area, int nivelMinimo){
        boolean autorizado = nivelUsuario >= nivelMinimo;
        registrarAcesso(usuario, area, autorizado);
        if(!autorizado){
            IO.print("\n ACESSO NEGADO! VOCÊ NÃO TEM PERMISSAO PARA ACESSAR: " + area + "\n");
        }
        return autorizado;
    }

    static String[] buscarUsuario(String nome){
        for(String[] u : usuarios)
            if (u[0].equals(nome)) return u;
        return null;
    }

    static String nomePerfil(int nivel){
        if(nivel == ADMINISTRADOR) return "Adminis trador";
        if(nivel == FUNCIONARIO) return "Funcionário";
        return "Visitante";
    }

    static void login() throws IOException{
        Scanner scanner = new Scanner(System.in);

        IO.print("\n╔════════════════════════════════════════════╗\n");
        IO.print("║     TELA DE LOGIN - Farmácia Viscondi      ║\n");
        IO.print("╚════════════════════════════════════════════╝\n\n");
        IO.print("MENU PRINCIPAL: \n\n");
        IO.print("[1] ENTRAR NO SISTEMA\n");
        IO.print("[2] ENCERRAR SISTEMA\n");
        IO.print("\n➤ESCOLHA UMA OPÇÃO: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        while(opcao < 1 || opcao > 2){
            IO.print("\nOPÇÃO INVÁLIDA\n\n");
            IO.print("➤DIGITE NOVAMENTE: ");
            opcao = scanner.nextInt();
            scanner.nextLine();
        }

        if(opcao == 1) {
            IO.print("DIGITE O USUÁRIO\n➤");
            String user = scanner.nextLine();

            while (buscarUsuario(user) == null){
                registrarAcesso(user, "LOGIN", false);
                IO.print("\n USUÁRIO NÃO CADASTRADO \n");
                IO.print("➤ TENTE NOVAMENTE: ");
                user = scanner.nextLine();
            }

            IO.print("\nDIGITE A SENHA\n➤");
            String senha = scanner.nextLine();

            String[] dadosUser = buscarUsuario(user);
            boolean senhaCorreta = dadosUser[1].equals(senha);

            int tentativas = 3;
            while (!senhaCorreta && tentativas > 0) {
                tentativas--;
                registrarAcesso(user, "LOGIN", false);
                if (tentativas == 0) {
                    IO.print("\n╔════════════════════════════════════════════╗\n");
                    IO.print("║    LIMITE MÁXIMO DE TENTATIVAS ALCANÇADO   ║\n");
                    IO.print("╚════════════════════════════════════════════╝\n");
                    return;
                }
                IO.print("\nSENHA INCORRETA! VOCÊ TEM MAIS " + tentativas + " TENTATIVA(S)\n");
                IO.print("TENTE NOVAMENTE: ");
                senha = scanner.nextLine();
                senhaCorreta = dadosUser[1].equals(senha);
            }

            registrarAcesso(user, "LOGIN", true);
            int nivel = Integer.parseInt(dadosUser[2]);

            IO.print("\nLOGIN REALIZADO COM SUCESSO!\n");
            IO.print("BEM-VINDO(A) DE VOLTA, " + user + "! [" + nomePerfil(nivel) + "]\n");

            if(nivel == ADMINISTRADOR){
                menuAdministrador(scanner, user, nivel);
            } else if (nivel == FUNCIONARIO) {
                menuFuncionario(scanner, user, nivel);
            } else {
                menuCliente(scanner, user, nivel);
            }

        } else{
            IO.print("\n╔════════════════════════════════════════════╗\n");
            IO.print("║     ✅ SISTEMA ENCERRADO COM SUCESSO ✅   ║\n");
            IO.print("║          Obrigado por utilizar!            ║\n");
            IO.print("╚════════════════════════════════════════════╝\n\n");
        }
    }

    //MENU ADMINISTRATIVO
    static void menuAdministrador(Scanner scanner, String user, int nivel){
        int op;
        do{
            IO.print("\n╔════════════════════════════════════════════╗\n");
            IO.print("║   MENU ADMINISTRATIVO - Farmácia Viscondi  ║\n");
            IO.print("╚════════════════════════════════════════════╝\n\n");
            IO.print("MENU PRINCIPAL: \n\n");
            IO.print("[1] ACESSAR LOJA\n");
            IO.print("[2] CARRINHO\n");
            IO.print("[3] PEDIDOS\n");
            IO.print("[4] ESTOQUE\n");
            IO.print("[5] GERENCIAR USUÁRIOS\n");
            IO.print("[6] HISTÓRICO DE ACESSOS\n");
            IO.print("[0] SAIR\n");
            IO.print("\n➤ESCOLHA UMA OPÇÃO: ");
            op = scanner.nextInt(); scanner.nextLine();

            switch (op) {
                case 1:
                    if (temPermissao(user, nivel, "Loja", VISITANTE)) acessarLoja(scanner, user, nivel);
                    break;
                case 2:
                    if (temPermissao(user, nivel, "Carrinho", VISITANTE)) acessarCarrinho(scanner, user, nivel);
                    break;
                case 3:
                    if (temPermissao(user, nivel, "Pedidos", VISITANTE)) acessarPedidos(scanner, user, nivel);
                    break;
                case 4:
                    if (temPermissao(user, nivel, "Estoque", FUNCIONARIO)) acessarEstoque(scanner, user, nivel);
                    break;
                case 5:
                    if (temPermissao(user, nivel, "Gerenciar Usuários", ADMINISTRADOR)) gerenciarUsuarios(scanner, user);
                    break;
                case 6:
                    if (temPermissao(user, nivel, "Histórico de Acessos", ADMINISTRADOR)) exibirHistorico();
                    break;
                case 0: break;
                default: IO.print("\nOPÇÃO INVÁLIDA\n");
            }
        } while(op != 0);
    }

    //MENU FUNCIONÁRIO
    static void menuFuncionario(Scanner scanner, String user, int nivel) {
        int op;
        do {
            IO.print("\n╔════════════════════════════════════════════╗\n");
            IO.print("║    MENU FUNCIONÁRIO - Farmácia Viscondi    ║\n");
            IO.print("╚════════════════════════════════════════════╝\n\n");
            IO.print("MENU PRINCIPAL: \n\n");
            IO.print("[1] ACESSAR LOJA\n");
            IO.print("[2] CARRINHO\n");
            IO.print("[3] PEDIDOS\n");
            IO.print("[4] ESTOQUE\n");
            IO.print("[0] SAIR\n");
            IO.print("\n➤ESCOLHA UMA OPÇÃO: ");
            op = scanner.nextInt(); scanner.nextLine();

            switch (op) {
                case 1:
                    if (temPermissao(user, nivel, "Loja", VISITANTE)) acessarLoja(scanner, user, nivel);
                    break;
                case 2:
                    if (temPermissao(user, nivel, "Carrinho", VISITANTE)) acessarCarrinho(scanner, user, nivel);
                    break;
                case 3:
                    if (temPermissao(user, nivel, "Pedidos", VISITANTE)) acessarPedidos(scanner, user, nivel);
                    break;
                case 4:
                    if (temPermissao(user, nivel, "Estoque", FUNCIONARIO)) acessarEstoque(scanner, user, nivel);
                    break;
                case 0: break;
                default: IO.print("\nOPÇÃO INVÁLIDA\n");
            }
        } while (op != 0);
    }

    //MENU CLIENTE
    static void menuCliente(Scanner scanner, String user, int nivel) {
        int op;
        do {
            IO.print("\n╔════════════════════════════════════════════╗\n");
            IO.print("║     MENU DO CLIENTE - Farmácia Viscondi    ║\n");
            IO.print("╚════════════════════════════════════════════╝\n\n");
            IO.print("MENU PRINCIPAL: \n\n");
            IO.print("[1] ACESSAR LOJA\n");
            IO.print("[2] CARRINHO\n");
            IO.print("[3] PEDIDOS\n");
            IO.print("[0] SAIR\n");
            IO.print("\n➤ESCOLHA UMA OPÇÃO: ");
            op = scanner.nextInt(); scanner.nextLine();

            switch (op) {
                case 1:
                    if (temPermissao(user, nivel, "Loja", VISITANTE)) acessarLoja(scanner, user, nivel);
                    break;
                case 2:
                    if (temPermissao(user, nivel, "Carrinho", VISITANTE)) acessarCarrinho(scanner, user, nivel);
                    break;
                case 3:
                    if (temPermissao(user, nivel, "Pedidos", VISITANTE)) acessarPedidos(scanner, user, nivel);
                    break;
                case 0: break;
                default: IO.print("\nOPÇÃO INVÁLIDA\n");
            }
        } while (op != 0);
    }

    static void gerenciarUsuarios(Scanner scanner, String adminAtual) {
        int op;
        do {
            IO.print("\n╔════════════════════════════════════════════╗\n");
            IO.print("║           GERENCIAR USUÁRIOS               ║\n");
            IO.print("╚════════════════════════════════════════════╝\n\n");
            IO.print("[1] LISTAR USUÁRIOS\n");
            IO.print("[2] CADASTRAR USUÁRIO\n");
            IO.print("[3] EDITAR USUÁRIO\n");
            IO.print("[4] REMOVER USUÁRIO\n");
            IO.print("[0] VOLTAR\n");
            IO.print("\n➤ESCOLHA UMA OPÇÃO: ");
            op = scanner.nextInt(); scanner.nextLine();

            switch (op) {
                case 1: listarUsuarios();                    break;
                case 2: cadastrarUsuario(scanner);           break;
                case 3: editarUsuario(scanner);              break;
                case 4: removerUsuario(scanner, adminAtual); break;
                case 0: break;
                default: IO.print("\nOPÇÃO INVÁLIDA\n");
            }
        } while (op != 0);
    }

    static void listarUsuarios() {
        IO.print("\n── USUÁRIOS CADASTRADOS ──────────────────────\n");
        for (int i = 0; i < usuarios.size(); i++) {
            String[] u = usuarios.get(i);
            IO.print("[" + (i + 1) + "] " + u[0] + "  |  Perfil: " + nomePerfil(Integer.parseInt(u[2])) + "\n");
        }
    }

    static void cadastrarUsuario(Scanner scanner) {
        IO.print("\n── CADASTRAR USUÁRIO ─────────────────────────\n");
        IO.print("Nome: ");
        String nome = scanner.nextLine();

        if (buscarUsuario(nome) != null) {
            IO.print("⚠️ Usuário já existe!\n");
            return;
        }

        IO.print("Senha: ");
        String senha = scanner.nextLine();

        IO.print("Perfil: [1] VISITANTE  [2] FUNCIONÁRIO  [3] ADMINISTRADOR\n➤");
        int nv = scanner.nextInt(); scanner.nextLine();
        int nivel = (nv == 3) ? ADMINISTRADOR : (nv == 2) ? FUNCIONARIO : VISITANTE;

        usuarios.add(new String[]{nome, senha, String.valueOf(nivel)});
        IO.print("✅ Usuário " + nome + " cadastrado com sucesso!\n");
    }

    static void editarUsuario(Scanner scanner) {
        listarUsuarios();
        IO.print("\nNúmero do usuário a editar (0 = cancelar): ");
        int idx = scanner.nextInt() - 1; scanner.nextLine();
        if (idx < 0 || idx >= usuarios.size()) { IO.print("Cancelado.\n"); return; }

        String[] u = usuarios.get(idx);
        IO.print("Editando: " + u[0] + "\n");

        IO.print("Novo nome (Enter para manter): ");
        String novoNome = scanner.nextLine();
        if (!novoNome.isBlank()) u[0] = novoNome;

        IO.print("Nova senha (Enter para manter): ");
        String novaSenha = scanner.nextLine();
        if (!novaSenha.isBlank()) u[1] = novaSenha;

        IO.print("Novo perfil [1]VISITANTE [2]FUNCIONÁRIO [3]ADMINISTRADOR (Enter p/ manter): ");
        String nv = scanner.nextLine();
        if (nv.equals("1")) u[2] = String.valueOf(VISITANTE);
        if (nv.equals("2")) u[2] = String.valueOf(FUNCIONARIO);
        if (nv.equals("3")) u[2] = String.valueOf(ADMINISTRADOR);

        IO.print("✅ Usuário atualizado com sucesso!\n");
    }

    static void removerUsuario(Scanner scanner, String adminAtual) {
        listarUsuarios();
        IO.print("\nNúmero do usuário a remover (0 = cancelar): ");
        int idx = scanner.nextInt() - 1; scanner.nextLine();
        if (idx < 0 || idx >= usuarios.size()) { IO.print("Cancelado.\n"); return; }

        String[] alvo = usuarios.get(idx);
        if (alvo[0].equals(adminAtual)) {
            IO.print("⛔ Você não pode remover sua própria conta!\n");
            return;
        }
        IO.print("✅ Usuário " + alvo[0] + " removido com sucesso!\n");
        usuarios.remove(idx);
    }

    // ══════════════════════════════════════════════
    //  HISTÓRICO  (somente Administrador)
    // ══════════════════════════════════════════════

    static void exibirHistorico() {
        IO.print("\n╔════════════════════════════════════════════╗\n");
        IO.print("║          HISTÓRICO DE ACESSOS              ║\n");
        IO.print("╚════════════════════════════════════════════╝\n");
        if (historico.isEmpty()) {
            IO.print("Nenhum registro encontrado.\n");
            return;
        }
        for (String reg : historico) IO.print(reg + "\n");
    }


    public static void main(String[] args) throws IOException {
        login();
    }
}