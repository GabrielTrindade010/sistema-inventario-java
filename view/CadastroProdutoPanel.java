package view;

import controller.ProdutoController;
import model.Produto;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Painel para cadastro e edição de produtos
 */
public class CadastroProdutoPanel extends JPanel {

    private JTextField txtNome;
    private JTextField txtCategoria;
    private JTextField txtQuantidade;
    private JTextField txtValor;
    private JButton btnSalvar;
    private JButton btnLimpar;

    private ProdutoController produtoController;
    private DashboardView dashboardView;
    private Produto produtoEdicao;

    public CadastroProdutoPanel(DashboardView dashboardView) {
        this.dashboardView = dashboardView;
        this.produtoController = new ProdutoController();
        initComponents();
    }

    public CadastroProdutoPanel(DashboardView dashboardView, Produto produto) {
        this.dashboardView = dashboardView;
        this.produtoController = new ProdutoController();
        this.produtoEdicao = produto;
        initComponents();
        preencherCampos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Painel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setPreferredSize(new Dimension(750, 60));

        String titulo = produtoEdicao == null ? "Cadastro de Produto" : "Edição de Produto";
        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);

        add(titlePanel, BorderLayout.NORTH);

        // Painel de formulário
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel lblNome = new JLabel("Nome do Produto:*");
        lblNome.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblNome, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        txtNome = new JTextField(30);
        txtNome.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtNome, gbc);

        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblCategoria = new JLabel("Categoria:*");
        lblCategoria.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblCategoria, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        txtCategoria = new JTextField(30);
        txtCategoria.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtCategoria, gbc);

        // Quantidade
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel lblQuantidade = new JLabel("Quantidade:*");
        lblQuantidade.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblQuantidade, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        txtQuantidade = new JTextField(30);
        txtQuantidade.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtQuantidade, gbc);

        // Valor
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel lblValor = new JLabel("Valor (R$):*");
        lblValor.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblValor, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        txtValor = new JTextField(30);
        txtValor.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtValor, gbc);

        // Legenda
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JLabel lblObrigatorio = new JLabel("* Campos obrigatórios");
        lblObrigatorio.setFont(new Font("Arial", Font.ITALIC, 12));
        lblObrigatorio.setForeground(Color.RED);
        formPanel.add(lblObrigatorio, gbc);

        // Botões
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setPreferredSize(new Dimension(150, 40));
        btnSalvar.addActionListener(e -> salvarProduto());
        formPanel.add(btnSalvar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Arial", Font.BOLD, 14));
        btnLimpar.setBackground(new Color(149, 165, 166));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setPreferredSize(new Dimension(150, 40));
        btnLimpar.addActionListener(e -> limparCampos());
        formPanel.add(btnLimpar, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void preencherCampos() {
        if (produtoEdicao != null) {
            txtNome.setText(produtoEdicao.getNome());
            txtCategoria.setText(produtoEdicao.getCategoria());
            txtQuantidade.setText(produtoEdicao.getQuantidade().toString());
            txtValor.setText(produtoEdicao.getValor().toString());
        }
    }

    private void salvarProduto() {
        try {
            String nome = txtNome.getText().trim();
            String categoria = txtCategoria.getText().trim();
            String quantidadeStr = txtQuantidade.getText().trim();
            String valorStr = txtValor.getText().trim().replace(",", ".");

            if (nome.isEmpty() || categoria.isEmpty() ||
                    quantidadeStr.isEmpty() || valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, preencha todos os campos obrigatórios",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer quantidade = Integer.parseInt(quantidadeStr);
            BigDecimal valor = new BigDecimal(valorStr);

            boolean sucesso;
            if (produtoEdicao == null) {
                // Cadastro
                sucesso = produtoController.cadastrarProduto(nome, categoria, quantidade, valor);
            } else {
                // Edição
                sucesso = produtoController.atualizarProduto(
                        produtoEdicao.getId(), nome, categoria, quantidade, valor);
            }

            if (sucesso) {
                String mensagem = produtoEdicao == null ?
                        "Produto cadastrado com sucesso!" :
                        "Produto atualizado com sucesso!";

                JOptionPane.showMessageDialog(this,
                        mensagem,
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                limparCampos();
                dashboardView.refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar produto",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade e Valor devem ser números válidos",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar produto: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCategoria.setText("");
        txtQuantidade.setText("");
        txtValor.setText("");
        txtNome.requestFocus();
        produtoEdicao = null;
    }
}