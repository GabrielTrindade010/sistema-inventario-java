package view;

import controller.ProdutoController;
import model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel para consulta de produtos
 */
public class ConsultaProdutoPanel extends JPanel {

    private JTextField txtBusca;
    private JButton btnBuscar;
    private JButton btnTodos;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnEditar;
    private JButton btnExcluir;

    private ProdutoController produtoController;
    private DashboardView dashboardView;

    public ConsultaProdutoPanel(DashboardView dashboardView) {
        this.dashboardView = dashboardView;
        this.produtoController = new ProdutoController();
        initComponents();
        carregarTodosProdutos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Painel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setPreferredSize(new Dimension(750, 60));

        JLabel lblTitle = new JLabel("Consulta de Produtos");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);

        add(titlePanel, BorderLayout.NORTH);

        // Painel de busca
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblBusca = new JLabel("Buscar por nome:");
        lblBusca.setFont(new Font("Arial", Font.BOLD, 14));
        searchPanel.add(lblBusca);

        txtBusca = new JTextField(30);
        txtBusca.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(txtBusca);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> buscarProdutos());
        searchPanel.add(btnBuscar);

        btnTodos = new JButton("Todos");
        btnTodos.setFont(new Font("Arial", Font.BOLD, 14));
        btnTodos.setBackground(new Color(149, 165, 166));
        btnTodos.setForeground(Color.WHITE);
        btnTodos.setFocusPainted(false);
        btnTodos.addActionListener(e -> carregarTodosProdutos());
        searchPanel.add(btnTodos);

        add(searchPanel, BorderLayout.NORTH);

        // Painel da tabela
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"ID", "Nome", "Categoria", "Quantidade", "Valor (R$)"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnEditar = new JButton("Editar");
        btnEditar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEditar.setBackground(new Color(241, 196, 15));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setPreferredSize(new Dimension(120, 35));
        btnEditar.addActionListener(e -> editarProduto());
        buttonPanel.add(btnEditar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setFont(new Font("Arial", Font.BOLD, 14));
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setPreferredSize(new Dimension(120, 35));
        btnExcluir.addActionListener(e -> excluirProduto());
        buttonPanel.add(btnExcluir);

        add(buttonPanel, BorderLayout.SOUTH);

        // Enter para buscar
        txtBusca.addActionListener(e -> buscarProdutos());
    }

    private void carregarTodosProdutos() {
        try {
            List<Produto> produtos = produtoController.listarTodos();
            atualizarTabela(produtos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar produtos: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProdutos() {
        try {
            String busca = txtBusca.getText().trim();
            List<Produto> produtos;

            if (busca.isEmpty()) {
                produtos = produtoController.listarTodos();
            } else {
                produtos = produtoController.buscarPorNome(busca);
            }

            atualizarTabela(produtos);

            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum produto encontrado",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar produtos: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela(List<Produto> produtos) {
        tableModel.setRowCount(0);

        for (Produto produto : produtos) {
            Object[] linha = {
                    produto.getId(),
                    produto.getNome(),
                    produto.getCategoria(),
                    produto.getQuantidade(),
                    String.format("%.2f", produto.getValor())
            };
            tableModel.addRow(linha);
        }
    }

    private void editarProduto() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um produto para editar",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Produto produto = produtoController.buscarPorId(id);

        if (produto != null) {
            // Abrir painel de edição
            JPanel contentPanel = (JPanel) dashboardView.getContentPane().getComponent(2);
            contentPanel.removeAll();
            CadastroProdutoPanel panel = new CadastroProdutoPanel(dashboardView, produto);
            contentPanel.add(panel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    private void excluirProduto() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um produto para excluir",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nome = (String) tableModel.getValueAt(selectedRow, 1);

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o produto:\n" + nome + "?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = produtoController.excluirProduto(id);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Produto excluído com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);

                    carregarTodosProdutos();
                    dashboardView.refreshDashboard();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir produto",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir produto: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}