package view;

import controller.ProdutoController;
import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard - Tela principal do sistema
 */
public class DashboardView extends JFrame {

    private Usuario usuarioLogado;
    private ProdutoController produtoController;
    private UsuarioController usuarioController;

    private JLabel lblTotalProdutos;
    private JLabel lblTotalUsuarios;
    private JPanel contentPanel;

    public DashboardView(Usuario usuario) {
        this.usuarioLogado = usuario;
        this.produtoController = new ProdutoController();
        this.usuarioController = new UsuarioController();
        initComponents();
        atualizarIndicadores();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Sistema de Inventário - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        // Painel superior (cabeçalho)
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Painel lateral (menu)
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.WEST);

        // Painel central (conteúdo)
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        showDashboardContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(1000, 60));

        JLabel lblTitle = new JLabel("  SISTEMA DE INVENTÁRIO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("Usuário: " + usuarioLogado.getNome() + " (" +
                usuarioLogado.getTipoUsuario() + ")  ");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUser.setForeground(Color.WHITE);

        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(lblUser, BorderLayout.EAST);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(52, 73, 94));
        panel.setPreferredSize(new Dimension(250, 700));

        // Botão Dashboard
        JButton btnDashboard = createMenuButton("📊 Dashboard");
        btnDashboard.addActionListener(e -> showDashboardContent());
        panel.add(btnDashboard);

        // Botão Cadastrar Produto
        JButton btnCadProduto = createMenuButton("➕ Cadastrar Produto");
        btnCadProduto.addActionListener(e -> showCadastroProduto());
        panel.add(btnCadProduto);

        // Botão Consultar Produtos
        JButton btnConsProduto = createMenuButton("📦 Consultar Produtos");
        btnConsProduto.addActionListener(e -> showConsultaProdutos());
        panel.add(btnConsProduto);

        // Botões de usuário (apenas para ADMIN)
        if (usuarioLogado.isAdmin()) {
            JButton btnCadUsuario = createMenuButton("👤 Cadastrar Usuário");
            btnCadUsuario.addActionListener(e -> showCadastroUsuario());
            panel.add(btnCadUsuario);

            JButton btnConsUsuario = createMenuButton("👥 Consultar Usuários");
            btnConsUsuario.addActionListener(e -> showConsultaUsuarios());
            panel.add(btnConsUsuario);
        }

        // Botão Backup
        JButton btnBackup = createMenuButton("💾 Backup");
        btnBackup.addActionListener(e -> showBackup());
        panel.add(btnBackup);

        // Espaço vazio
        panel.add(Box.createVerticalGlue());

        // Botão Sair
        JButton btnSair = createMenuButton("🚪 Sair");
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.addActionListener(e -> sair());
        panel.add(btnSair);

        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getText().contains("Sair")) {
                    button.setBackground(new Color(52, 73, 94));
                }
            }
        });

        return button;
    }

    private void showDashboardContent() {
        contentPanel.removeAll();

        JPanel dashPanel = new JPanel();
        dashPanel.setLayout(new GridBagLayout());
        dashPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Bem-vindo ao Sistema de Inventário");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        dashPanel.add(lblTitulo, gbc);

        // Card Total de Produtos
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JPanel cardProdutos = createDashboardCard("Total de Produtos", "0", new Color(46, 204, 113));

        for (Component comp : cardProdutos.getComponents()) {
            if (comp instanceof JLabel) {
                lblTotalProdutos = (JLabel) comp;
                break;
            }
        }

        dashPanel.add(cardProdutos, gbc);

        // Card Total de Usuários
        gbc.gridx = 1;
        gbc.gridy = 1;
        JPanel cardUsuarios = createDashboardCard("Total de Usuários", "0", new Color(52, 152, 219));

        for (Component comp : cardUsuarios.getComponents()) {
            if (comp instanceof JLabel) {
                lblTotalUsuarios = (JLabel) comp;
                break;
            }
        }

        dashPanel.add(cardUsuarios, gbc);

        contentPanel.add(dashPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();

        atualizarIndicadores();
    }

    private JPanel createDashboardCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setPreferredSize(new Dimension(300, 150));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 48));
        lblValue.setForeground(Color.WHITE);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createVerticalGlue());
        card.add(lblValue);

        return card;
    }

    private void atualizarIndicadores() {
        try {
            int totalProdutos = produtoController.contarProdutos();
            int totalUsuarios = usuarioController.contarUsuariosAtivos();

            if (lblTotalProdutos != null) {
                lblTotalProdutos.setText(String.valueOf(totalProdutos));
            }
            if (lblTotalUsuarios != null) {
                lblTotalUsuarios.setText(String.valueOf(totalUsuarios));
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar indicadores: " + e.getMessage());
        }
    }

    private void showCadastroProduto() {
        contentPanel.removeAll();
        CadastroProdutoPanel panel = new CadastroProdutoPanel(this);
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showConsultaProdutos() {
        contentPanel.removeAll();
        ConsultaProdutoPanel panel = new ConsultaProdutoPanel(this);
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCadastroUsuario() {
        contentPanel.removeAll();
        CadastroUsuarioPanel panel = new CadastroUsuarioPanel(this);
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showConsultaUsuarios() {
        contentPanel.removeAll();
        ConsultaUsuarioPanel panel = new ConsultaUsuarioPanel(this);
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showBackup() {
        contentPanel.removeAll();
        BackupPanel panel = new BackupPanel();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void sair() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente sair do sistema?",
                "Confirmar Saída",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            dispose();
            LoginView login = new LoginView();
            login.setVisible(true);
        }
    }

    public void refreshDashboard() {
        atualizarIndicadores();
    }
}