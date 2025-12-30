package view;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de Login do Sistema
 */
public class LoginView extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnSair;

    private UsuarioController usuarioController;

    public LoginView() {
        this.usuarioController = new UsuarioController();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Sistema de Inventário - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setResizable(false);

        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));

        // Painel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setPreferredSize(new Dimension(400, 80));

        JLabel lblTitle = new JLabel("SISTEMA DE INVENTÁRIO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);

        // Painel de formulário
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Login
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblLogin, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        txtLogin = new JTextField(20);
        txtLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtLogin, gbc);

        // Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblSenha, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        txtSenha = new JPasswordField(20);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtSenha, gbc);

        // Botões
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEntrar.setBackground(new Color(46, 204, 113));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.addActionListener(e -> realizarLogin());
        formPanel.add(btnEntrar, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 14));
        btnSair.setBackground(new Color(231, 76, 60));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(e -> System.exit(0));
        formPanel.add(btnSair, gbc);

        // Adicionar painéis ao frame
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Enter para fazer login
        txtSenha.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha todos os campos",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = usuarioController.autenticar(login, senha);

            if (usuario != null) {
                JOptionPane.showMessageDialog(this,
                        "Login realizado com sucesso!\nBem-vindo, " + usuario.getNome(),
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                // Abrir Dashboard
                DashboardView dashboard = new DashboardView(usuario);
                dashboard.setVisible(true);

                // Fechar tela de login
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Login ou senha incorretos, ou usuário inativo",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                txtSenha.setText("");
                txtLogin.requestFocus();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao realizar login: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginView login = new LoginView();
            login.setVisible(true);
        });
    }
}