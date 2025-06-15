import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

interface IItemOperations {
    void adicionarQuantidade(int quantidade);
    void removerQuantidade(int quantidade) throws QuantidadeInsuficienteException;
    void visualizar();
}

class QuantidadeInsuficienteException extends Exception {
    public QuantidadeInsuficienteException(String mensagem) {
        super(mensagem);
    }
}

class ItemCategoriaRestritaException extends Exception {
    public ItemCategoriaRestritaException(String mensagem) {
        super(mensagem);
    }
}

class Encantamento implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private int nivel;

    public Encantamento(String nome, int nivel) {
        this.nome = nome;
        if (nivel < 1 || nivel > 5) {
            throw new IllegalArgumentException("Nível de encantamento deve ser entre 1 e 5.");
        }
        this.nivel = nivel;
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return nome + " " + nivel;
    }
}

abstract class Item implements IItemOperations, Serializable {
    private static final long serialVersionUID = 1L;
    protected String nome;
    protected int quantidade;
    protected Categoria categoria;
    protected String chave;
    protected Encantamento encantamento;

    public Item(String nome, int quantidade, Categoria categoria, String chave) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.categoria = categoria;
        this.chave = chave;
        this.encantamento = null;
    }

    public String getNome() {
        return nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getChave() {
        return chave;
    }

    public Encantamento getEncantamento() {
        return encantamento;
    }

    public void setEncantamento(Encantamento encantamento) {
        this.encantamento = encantamento;
    }

    protected String getDescricaoEncantamento() {
        if (encantamento != null) {
            return " [Encantado: " + encantamento.toString() + "]";
        }
        return "";
    }

    @Override
    public void adicionarQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade adicionada deve ser positiva.");
        }
        this.quantidade += quantidade;
    }

    @Override
    public void removerQuantidade(int quantidade) throws QuantidadeInsuficienteException {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade removida deve ser positiva.");
        }
        if (this.quantidade < quantidade) {
            throw new QuantidadeInsuficienteException("Quantidade insuficiente para remover.");
        }
        this.quantidade -= quantidade;
    }

    @Override
    public abstract void visualizar();

    public abstract String detalhes();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}

class ItemComum extends Item {
    private static final long serialVersionUID = 1L;

    public ItemComum(String nome, int quantidade, Categoria categoria) {
        super(nome, quantidade, categoria, UUID.randomUUID().toString());
    }

    @Override
    public void visualizar() {
        System.out.println("- Item: " + nome + getDescricaoEncantamento() + " | Quantidade: " + quantidade + " | Categoria: " + categoria.getNome());
    }

    @Override
    public String detalhes() {
        return "ItemComum{" +
                "nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", categoria=" + categoria.getNome() +
                getDescricaoEncantamento() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemComum)) return false;
        ItemComum that = (ItemComum) o;
        return nome.equalsIgnoreCase(that.nome) && categoria.getCodigo() == that.categoria.getCodigo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase(), categoria.getCodigo());
    }
}

class ItemComDurabilidade extends Item {
    private static final long serialVersionUID = 1L;
    private int durabilidade;

    public ItemComDurabilidade(String nome, int quantidade, Categoria categoria, int durabilidade) {
        super(nome, quantidade, categoria, UUID.randomUUID().toString());
        this.durabilidade = durabilidade;
        if (durabilidade < 0 || durabilidade > 1500)
            throw new IllegalArgumentException("Durabilidade deve estar entre 0 e 1500.");
    }

    public int getDurabilidade() {
        return durabilidade;
    }

    public void setDurabilidade(int durabilidade) {
        if (durabilidade < 0 || durabilidade > 1500)
            throw new IllegalArgumentException("Durabilidade deve estar entre 0 e 1500.");
        this.durabilidade = durabilidade;
    }

    @Override
    public void visualizar() {
        System.out.println("- Item: " + nome + getDescricaoEncantamento() + " | Quantidade: " + quantidade +
            " | Categoria: " + categoria.getNome() + " | Durabilidade: " + durabilidade);
    }

    @Override
    public String detalhes() {
        return "ItemComDurabilidade{" +
                "nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", categoria=" + categoria.getNome() +
                ", durabilidade=" + durabilidade +
                getDescricaoEncantamento() +
                ", chave=" + chave +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemComDurabilidade)) return false;
        ItemComDurabilidade that = (ItemComDurabilidade) o;

        return durabilidade == that.durabilidade &&
                chave.equals(that.chave) &&
                nome.equalsIgnoreCase(that.nome) &&
                categoria.getCodigo() == that.categoria.getCodigo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase(), categoria.getCodigo(), durabilidade, chave);
    }
}

class ItemEspecial extends Item {
    private static final long serialVersionUID = 1L;
    private String efeito;

    public ItemEspecial(String nome, int quantidade, Categoria categoria, String efeito) {
        super(nome, quantidade, categoria, UUID.randomUUID().toString());
        this.efeito = efeito;
    }

    public String getEfeito() {
        return efeito;
    }

    public void setEfeito(String efeito) {
        this.efeito = efeito;
    }

    @Override
    public void visualizar() {
        System.out.println("- Item: " + nome + getDescricaoEncantamento() + " | Quantidade: " + quantidade +
            " | Categoria: " + categoria.getNome() + " | Efeito: " + efeito);
    }

    @Override
    public String detalhes() {
        return "ItemEspecial{" +
                "nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", categoria=" + categoria.getNome() +
                ", efeito='" + efeito + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemEspecial)) return false;
        ItemEspecial that = (ItemEspecial) o;
        return nome.equalsIgnoreCase(that.nome) &&
                categoria.getCodigo() == that.categoria.getCodigo() &&
                Objects.equals(efeito, that.efeito);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome.toLowerCase(), categoria.getCodigo(), efeito);
    }
}

class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private int codigo;
    private List<Item> itens;

    public Categoria(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
        this.itens = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void adicionarItem(Item item) {
        if (item != null && !itens.contains(item)) {
            itens.add(item);
        }
    }

    public void removerItem(Item item) {
        itens.remove(item);
    }

    public List<Item> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void visualizarItens() {
        System.out.println("Categoria: " + nome);
        if (itens.isEmpty()) {
            System.out.println("  Nenhum item nesta categoria.");
        } else {
            for (Item i : itens) {
                i.visualizar();
            }
        }
    }
}

class Inventario implements Serializable {
    private static final long serialVersionUID = 1L;
    List<Item> itens;
    List<Categoria> categorias;

    public static final Set<Integer> CODIGOS_CATEGORIAS_RESTRITAS_DURABILIDADE = new HashSet<>(Arrays.asList(4, 5, 7));
    public static final int MAX_ITENS_NAO_RESTRITOS = 64;

    public static final Set<String> ITENS_VALIDOS_COMIDA = new HashSet<>(Arrays.asList(
            "batata", "cenoura", "frango assado", "bife", "cenoura dourada", "maça dourada"));

    private static final Map<String, Double> REGEN_COMIDA = new HashMap<>();
    static {
        REGEN_COMIDA.put("batata", 0.5);
        REGEN_COMIDA.put("cenoura", 1.5);
        REGEN_COMIDA.put("frango assado", 3.0);
        REGEN_COMIDA.put("bife", 4.0);
        REGEN_COMIDA.put("cenoura dourada", 3.5);
        REGEN_COMIDA.put("maça dourada", 3.0);
    }

    public Inventario() {
        this.itens = new ArrayList<>();
        this.categorias = new ArrayList<>();
        inicializarCategoriasFixas();
    }

    private void inicializarCategoriasFixas() {
        adicionarCategoria(new Categoria(1, "blocos"));
        adicionarCategoria(new Categoria(2, "minerios"));
        adicionarCategoria(new Categoria(3, "comida"));
        adicionarCategoria(new Categoria(4, "armaduras"));
        adicionarCategoria(new Categoria(5, "ferramentas"));
        adicionarCategoria(new Categoria(6, "especial"));
        adicionarCategoria(new Categoria(7, "armas"));
    }

    private void adicionarCategoria(Categoria cat) {
        categorias.add(cat);
    }

    public Categoria obterCategoriaPorCodigo(int codigo) {
        for (Categoria c : categorias) {
            if (c.getCodigo() == codigo) return c;
        }
        return null;
    }

    public Categoria criarOuObterCategoria(String nomeCategoria) {
        nomeCategoria = nomeCategoria.trim().toLowerCase();
        for (Categoria c : categorias) {
            if (c.getNome().equalsIgnoreCase(nomeCategoria)) {
                return c;
            }
        }
        Categoria nova = new Categoria(proximoCodigoLivre(), nomeCategoria);
        categorias.add(nova);
        return nova;
    }

    private int proximoCodigoLivre() {
        int max = 0;
        for (Categoria c : categorias) {
            if (c.getCodigo() > max) max = c.getCodigo();
        }
        return max + 1;
    }

    private boolean eCategoriaRestritaPorCodigo(int codigoCategoria) {
        return CODIGOS_CATEGORIAS_RESTRITAS_DURABILIDADE.contains(codigoCategoria);
    }

    boolean eCategoriaComEncantamento(int codigoCategoria) {
        return eCategoriaRestritaPorCodigo(codigoCategoria);
    }

    public void adicionarItem(Item item) throws ItemCategoriaRestritaException {
        if (item == null) throw new IllegalArgumentException("Item não pode ser nulo.");
        int codigoCategoria = item.getCategoria() != null ? item.getCategoria().getCodigo() : -1;
        if (codigoCategoria == -1) throw new IllegalArgumentException("Item deve possuir categoria.");

        if (codigoCategoria == 3) {
            if (!ITENS_VALIDOS_COMIDA.contains(item.getNome().toLowerCase())) {
                throw new IllegalArgumentException("Item '" + item.getNome() + "' inválido para a categoria comida.");
            }
            if (item.getQuantidade() > MAX_ITENS_NAO_RESTRITOS) {
                throw new IllegalArgumentException("Não é possível adicionar mais de " + MAX_ITENS_NAO_RESTRITOS + " itens.");
            }
        }

        if (item instanceof ItemComDurabilidade) {
            Optional<Item> itemExistente = itens.stream()
                .filter(i ->
                    i instanceof ItemComDurabilidade &&
                    i.getNome().equalsIgnoreCase(item.getNome()) &&
                    i.getCategoria().getCodigo() == codigoCategoria &&
                    ((ItemComDurabilidade)i).getDurabilidade() == ((ItemComDurabilidade)item).getDurabilidade() &&
                    i.getChave().equals(item.getChave())
                )
                .findFirst();
            if (itemExistente.isPresent()) {
                itemExistente.get().adicionarQuantidade(item.getQuantidade());
                return;
            }
        } else {
            Optional<Item> itemExistente = itens.stream()
                .filter(i ->
                    !(i instanceof ItemComDurabilidade) &&
                    i.getNome().equalsIgnoreCase(item.getNome()) &&
                    i.getCategoria().getCodigo() == codigoCategoria)
                .findFirst();
            if (itemExistente.isPresent()) {
                if (itemExistente.get().getQuantidade() + item.getQuantidade() > MAX_ITENS_NAO_RESTRITOS) {
                    throw new IllegalArgumentException("Não é possível adicionar mais de " + MAX_ITENS_NAO_RESTRITOS + " itens.");
                }
                itemExistente.get().adicionarQuantidade(item.getQuantidade());
                return;
            }
        }

        if (!eCategoriaRestritaPorCodigo(codigoCategoria) && item.getQuantidade() > MAX_ITENS_NAO_RESTRITOS) {
            throw new IllegalArgumentException("Não é possível adicionar mais de " + MAX_ITENS_NAO_RESTRITOS + " itens.");
        }

        this.itens.add(item);
        if (!categorias.contains(item.getCategoria())) {
            categorias.add(item.getCategoria());
        }
        item.getCategoria().adicionarItem(item);
    }

    public void removerItem(String nomeItem, int codigoCategoria, int quantidade, Integer durabilidade) throws ItemNaoEncontradoException, QuantidadeInsuficienteException {
        Optional<Item> achado;

        if (durabilidade != null) {
            achado = itens.stream().filter(item ->
                item.getNome().equalsIgnoreCase(nomeItem) &&
                item.getCategoria().getCodigo() == codigoCategoria &&
                (item instanceof ItemComDurabilidade) &&
                ((ItemComDurabilidade) item).getDurabilidade() == durabilidade
            ).findFirst();
        } else {
            achado = itens.stream().filter(item ->
                item.getNome().equalsIgnoreCase(nomeItem) &&
                item.getCategoria().getCodigo() == codigoCategoria
            ).findFirst();
        }

        if (!achado.isPresent()) {
            throw new ItemNaoEncontradoException("Item '" + nomeItem + "' na categoria '" + obterCategoriaPorCodigo(codigoCategoria).getNome() + "' não encontrado no inventário.");
        }
        Item item = achado.get();
        item.removerQuantidade(quantidade);
        if (item.getQuantidade() == 0) {
            itens.remove(item);
            Categoria cat = item.getCategoria();
            if (cat != null) {
                cat.removerItem(item);
            }
        }
    }

    public void visualizarInventario() {
        System.out.println("=== Inventário ===");
        if (itens.isEmpty()) {
            System.out.println("Inventário vazio.");
            return;
        }
        Map<String, List<Item>> agrupadoPorCategoria = itens.stream().collect(Collectors.groupingBy(i -> i.getCategoria().getNome()));
        for (String catNome : agrupadoPorCategoria.keySet()) {
            System.out.println("Categoria: " + catNome);
            List<Item> itensCategoria = agrupadoPorCategoria.get(catNome);
            for (Item item : itensCategoria) {
                if ("comida".equalsIgnoreCase(catNome)) {
                    double regen = REGEN_COMIDA.getOrDefault(item.getNome().toLowerCase(), 0.0);
                    if ("maça dourada".equalsIgnoreCase(item.getNome())) {
                        System.out.printf("- Item: %s | Quantidade: %d | Categoria: %s | Regenera: %.1f corações, efeitos positivos no jogador%n",
                                item.getNome(), item.getQuantidade(), catNome, regen);
                    } else {
                        System.out.printf("- Item: %s | Quantidade: %d | Categoria: %s | Regenera: %.1f corações%n",
                                item.getNome(), item.getQuantidade(), catNome, regen);
                    }
                } else if (item instanceof ItemComDurabilidade) {
                    String encantamentoDesc = item.getEncantamento() != null ? " [Encantado: " + item.getEncantamento().toString() + "]" : "";
                    System.out.printf("- Item: %s%s | Quantidade: %d | Categoria: %s | Durabilidade: %d%n",
                        item.getNome(), encantamentoDesc, item.getQuantidade(), catNome, ((ItemComDurabilidade) item).getDurabilidade());
                } else {
                    item.visualizar();
                }
            }
            System.out.println();
        }
    }

    public void gerarRelatorio() {
        System.out.println("=== Relatório de Inventário ===");
        int totalItens = 0;
        for (Categoria cat : categorias) {
            int somaCat = cat.getItens().stream().mapToInt(Item::getQuantidade).sum();
            System.out.println("Categoria '" + cat.getNome() + "': " + somaCat + " itens no total.");
            totalItens += somaCat;
        }
        System.out.println("Total geral de itens no inventário: " + totalItens);
    }

    public void salvarInventarioEmArquivo(String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            for (Item item : itens) {
                String classe = "";
                String categoriaNome = item.getCategoria() != null ? item.getCategoria().getNome() : "";
                int codigoCategoria = item.getCategoria() != null ? item.getCategoria().getCodigo() : -1;
                String linha;

                String encantamentoStr = item.getEncantamento() != null ? item.getEncantamento().getNome() + "," + item.getEncantamento().getNivel() : "";

                if (item instanceof ItemComDurabilidade) {
                    ItemComDurabilidade icd = (ItemComDurabilidade) item;
                    classe = "ItemComDurabilidade";
                    linha = String.format("%s;%s;%d;%d;%d;%s;%s", classe, icd.getNome(), icd.getQuantidade(), codigoCategoria, icd.getDurabilidade(), icd.getChave(), encantamentoStr);
                } else if (item instanceof ItemComum) {
                    classe = "ItemComum";
                    linha = String.format("%s;%s;%d;%d;%s;%s", classe, item.getNome(), item.getQuantidade(), codigoCategoria, item.getChave(), encantamentoStr);
                } else if (item instanceof ItemEspecial) {
                    ItemEspecial ie = (ItemEspecial) item;
                    classe = "ItemEspecial";
                    linha = String.format("%s;%s;%d;%d;%s;%s;%s", classe, ie.getNome(), ie.getQuantidade(), codigoCategoria, ie.getEfeito(), ie.getChave(), encantamentoStr);
                } else {
                    continue;
                }
                writer.write(linha);
                writer.newLine();
            }
        }
    }

    public void carregarInventarioDeArquivo(String caminhoArquivo) throws IOException {
        itens.clear();
        categorias.clear();
        inicializarCategoriasFixas();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length < 6) continue;
                String tipo = partes[0];
                String nome = partes[1];
                int quantidade = Integer.parseInt(partes[2]);
                int codigoCategoria = Integer.parseInt(partes[3]);
                Categoria cat = obterCategoriaPorCodigo(codigoCategoria);
                if (cat == null) continue;

                try {
                    if ("ItemComDurabilidade".equals(tipo)) {
                        if (partes.length < 7) continue;
                        int durabilidade = Integer.parseInt(partes[4]);
                        String chave = partes[5];
                        String encantamentoInfo = partes.length > 6 ? partes[6] : null;
                        ItemComDurabilidade icd = new ItemComDurabilidade(nome, quantidade, cat, durabilidade);
                        icd.chave = chave;
                        if (encantamentoInfo != null && !encantamentoInfo.isEmpty()) {
                            String[] encParts = encantamentoInfo.split(",");
                            if (encParts.length == 2) {
                                try {
                                    int nivelEnc = Integer.parseInt(encParts[1]);
                                    icd.setEncantamento(new Encantamento(encParts[0], nivelEnc));
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                        adicionarItem(icd);
                    } else if ("ItemComum".equals(tipo)) {
                        String chave = partes[4];
                        String encantamentoInfo = partes.length > 5 ? partes[5] : null;
                        ItemComum ic = new ItemComum(nome, quantidade, cat);
                        ic.chave = chave;
                        if (encantamentoInfo != null && !encantamentoInfo.isEmpty()) {
                            String[] encParts = encantamentoInfo.split(",");
                            if (encParts.length == 2) {
                                try {
                                    int nivelEnc = Integer.parseInt(encParts[1]);
                                    ic.setEncantamento(new Encantamento(encParts[0], nivelEnc));
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                        adicionarItem(ic);
                    } else if ("ItemEspecial".equals(tipo)) {
                        if (partes.length < 7) continue;
                        String efeito = partes[4];
                        String chave = partes[5];
                        String encantamentoInfo = partes.length > 6 ? partes[6] : null;
                        ItemEspecial ie = new ItemEspecial(nome, quantidade, cat, efeito);
                        ie.chave = chave;
                        if (encantamentoInfo != null && !encantamentoInfo.isEmpty()) {
                            String[] encParts = encantamentoInfo.split(",");
                            if (encParts.length == 2) {
                                try {
                                    int nivelEnc = Integer.parseInt(encParts[1]);
                                    ie.setEncantamento(new Encantamento(encParts[0], nivelEnc));
                                } catch (NumberFormatException ignored) {}
                            }
                        }
                        adicionarItem(ie);
                    }
                } catch (ItemCategoriaRestritaException ex) {
                    System.out.println("[Atenção] Item não adicionado no carregamento: " + ex.getMessage());
                }
            }
        }
    }
}

class ItemNaoEncontradoException extends Exception {
    public ItemNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

class Usuario {
    private String nome;
    private Inventario inventario;

    public Usuario(String nome) {
        this.nome = nome;
        this.inventario = new Inventario();
    }

    public String getNome() {
        return nome;
    }

    public Inventario getInventario() {
        return inventario;
    }
}

public class MinecraftInventoryManagement {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Usuario usuario = new Usuario("Jogador1");
        Inventario inventario = usuario.getInventario();

        System.out.println("=== Sistema de Gerenciamento de Inventário Minecraft ===");
        System.out.println("Categorias pré-criadas:");
        for (Categoria c : inventario.categorias) {
            System.out.println(c.getCodigo() + " - " + c.getNome());
        }

        boolean executando = true;
        while (executando) {
            System.out.println("\nMenu Principal:");
            System.out.println("1 - Adicionar Item");
            System.out.println("2 - Remover Item");
            System.out.println("3 - Visualizar Inventário");
            System.out.println("4 - Gerar Relatório");
            System.out.println("5 - Salvar Inventário");
            System.out.println("6 - Carregar Inventário");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            try {
                switch (opcao) {
                    case "1":
                        adicionarItemMenu(inventario);
                        break;
                    case "2":
                        removerItemMenu(inventario);
                        break;
                    case "3":
                        inventario.visualizarInventario();
                        break;
                    case "4":
                        inventario.gerarRelatorio();
                        break;
                    case "5":
                        salvarInventarioMenu(inventario);
                        break;
                    case "6":
                        carregarInventarioMenu(inventario);
                        break;
                    case "0":
                        executando = false;
                        System.out.println("Encerrando o sistema. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("[Erro]: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void adicionarItemMenu(Inventario inventario) {
        System.out.println("Adicionar Item");
        System.out.println("Selecione a categoria do item:");
        for (Categoria c : inventario.categorias) {
            System.out.println(c.getCodigo() + " - " + c.getNome());
        }
        System.out.print("Digite o número da categoria: ");
        int codigoCategoria = lerInteiroPositivo();

        Categoria categoria = inventario.obterCategoriaPorCodigo(codigoCategoria);
        if (categoria == null) {
            System.out.println("Categoria inválida.");
            return;
        }

        String nome = "";
        if (codigoCategoria == 3) {
            System.out.println("Itens válidos para a categoria 'comida':");
            List<String> itensValidos = new ArrayList<>(Inventario.ITENS_VALIDOS_COMIDA);
            Collections.sort(itensValidos);
            for (int i = 0; i < itensValidos.size(); i++) {
                System.out.println((i + 1) + " - " + itensValidos.get(i));
            }
            System.out.print("Escolha um item pelo número: ");
            int escolhaItem = lerInteiroPositivoEntre(1, itensValidos.size());
            nome = itensValidos.get(escolhaItem - 1);
        } else {
            System.out.print("Nome do item: ");
            nome = scanner.nextLine().trim();
        }

        int quantidade = 1;
        boolean restrita = Inventario.CODIGOS_CATEGORIAS_RESTRITAS_DURABILIDADE.contains(codigoCategoria);
        if (!restrita) {
            System.out.print("Quantidade (máximo 64): ");
            quantidade = lerInteiroPositivoEntre(1, Inventario.MAX_ITENS_NAO_RESTRITOS);
        }

        try {
            if (restrita) {
                System.out.print("Durabilidade (0-1500): ");
                int durabilidade = lerInteiroPositivoEntre(0, 1500);
                ItemComDurabilidade item = new ItemComDurabilidade(nome, quantidade, categoria, durabilidade);
                if (inventario.eCategoriaComEncantamento(codigoCategoria)) {
                    System.out.print("Deseja aplicar um encantamento ao item? (s/n): ");
                    String resp = scanner.nextLine().trim().toLowerCase();
                    if ("s".equals(resp)) {
                        System.out.println("Escolha um encantamento:");
                        System.out.println("1 - Inquebrável (para armas, ferramentas e armaduras)");
                        System.out.println("2 - Proteção (apenas para armaduras)");
                        System.out.println("3 - Eficiência (apenas para ferramentas)");
                        System.out.println("4 - Afiamento (apenas para armas)");
                        System.out.print("Digite o número do encantamento: ");
                        int escolhaEncantamento = lerInteiroPositivoEntre(1, 4);
                        String nomeEncantamento = "";
                        switch (escolhaEncantamento) {
                            case 1:
                                nomeEncantamento = "Inquebrável";
                                break;
                            case 2:
                                if (codigoCategoria == 4) {
                                    nomeEncantamento = "Proteção";
                                } else {
                                    System.out.println("Encantamento inválido para esta categoria.");
                                    return;
                                }
                                break;
                            case 3:
                                if (codigoCategoria == 5) {
                                    nomeEncantamento = "Eficiência";
                                } else {
                                    System.out.println("Encantamento inválido para esta categoria.");
                                    return;
                                }
                                break;
                            case 4:
                                if (codigoCategoria == 7) {
                                    nomeEncantamento = "Afiamento";
                                } else {
                                    System.out.println("Encantamento inválido para esta categoria.");
                                    return;
                                }
                                break;
                            default:
                                System.out.println("Encantamento inválido.");
                                return;
                        }
                        System.out.print("Digite o nível do encantamento (1-5): ");
                        int nivel = lerInteiroPositivoEntre(1, 5);
                        item.setEncantamento(new Encantamento(nomeEncantamento, nivel));
                    }
                }
                inventario.adicionarItem(item);
                System.out.println("Item adicionado com sucesso!");
            } else if ("especial".equalsIgnoreCase(categoria.getNome())) {
                System.out.print("Efeito especial: ");
                String efeito = scanner.nextLine();
                ItemEspecial item = new ItemEspecial(nome, quantidade, categoria, efeito);
                inventario.adicionarItem(item);
                System.out.println("Item adicionado com sucesso!");
            } else {
                ItemComum item = new ItemComum(nome, quantidade, categoria);
                inventario.adicionarItem(item);
                System.out.println("Item adicionado com sucesso!");
            }
        } catch (ItemCategoriaRestritaException ex) {
            System.out.println("[Erro] Não foi possível adicionar o item: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("[Erro] " + ex.getMessage());
        }
    }

    private static void removerItemMenu(Inventario inventario) {
        System.out.println("Selecione a categoria do item a remover:");
        for (Categoria c : inventario.categorias) {
            System.out.println(c.getCodigo() + " - " + c.getNome());
        }
        System.out.print("Digite o número da categoria: ");
        int codigoCategoria = lerInteiroPositivo();

        Categoria categoria = inventario.obterCategoriaPorCodigo(codigoCategoria);
        if (categoria == null) {
            System.out.println("Categoria inválida.");
            return;
        }

        System.out.print("Nome do item a remover: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Quantidade a remover: ");
        int quantidadeRemover = lerInteiroPositivo();

        Integer durabilidade = null;
        if (Inventario.CODIGOS_CATEGORIAS_RESTRITAS_DURABILIDADE.contains(codigoCategoria)) {
            System.out.print("Durabilidade do item a remover (0-1500): ");
            durabilidade = lerInteiroPositivoEntre(0, 1500);
        }

        try {
            inventario.removerItem(nome, codigoCategoria, quantidadeRemover, durabilidade);
            System.out.println("Item removido com sucesso do inventário.");
        } catch (ItemNaoEncontradoException | QuantidadeInsuficienteException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void salvarInventarioMenu(Inventario inventario) {
        System.out.print("Digite o caminho do arquivo para salvar (ex: inventario.txt): ");
        String caminho = scanner.nextLine().trim();
        try {
            inventario.salvarInventarioEmArquivo(caminho);
            System.out.println("Inventário salvo com sucesso no arquivo '" + caminho + "'.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    private static void carregarInventarioMenu(Inventario inventario) {
        System.out.print("Digite o caminho do arquivo para carregar (ex: inventario.txt): ");
        String caminho = scanner.nextLine().trim();
        try {
            inventario.carregarInventarioDeArquivo(caminho);
            System.out.println("Inventário carregado com sucesso do arquivo '" + caminho + "'.");
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
        }
    }

    private static int lerInteiroPositivo() {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor <= 0) {
                    System.out.print("Digite um número inteiro positivo: ");
                    continue;
                }
                return valor;
            } catch (NumberFormatException ex) {
                System.out.print("Entrada inválida. Digite um número inteiro positivo: ");
            }
        }
    }

    private static int lerInteiroPositivoEntre(int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor < min || valor > max) {
                    System.out.print("Digite um número entre " + min + " e " + max + ": ");
                    continue;
                }
                return valor;
            } catch (NumberFormatException ex) {
                System.out.print("Entrada inválida. Digite um número entre " + min + " e " + max + ": ");
            }
        }
    }
}