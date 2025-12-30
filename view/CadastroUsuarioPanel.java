package view;

import controller.UsuarioController;
import model.Usuario;
import model.Usuario.TipoUsuario;

import javax.swing.*;
import java.awt.*;

/**
 * Painel para cadastro e edição de usuários
 */
public class CadastroUsuarioPanel extends JPanel {

    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmaSenha;
    private JComboBox<String> cmbTipoUsuario;
    private JCheckBox chkAtivo;
    private JButton btnSalvar;
    private JButton btnLimpar;

    private UsuarioController usuarioController;
    private DashboardView dashboardView;
    private Usuario usuarioEdicao;

    public CadastroUsuarioPanel(DashboardView dashboardView) {
        this.dashboardView = dashboardView;
        this.usuarioController = new UsuarioController();
        initComponents();
    }

    public CadastroUsuarioPanel(DashboardView dashboardView, Usuario usuario) {
        this.dashboardView = dashboardView;
        this.usuarioController = new UsuarioController();
        this.usuarioEdicao = usuario;
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

        String titulo = usuarioEdicao == null ? "Cadastro de Usuário" : "Edição de Usuário";
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
        JLabel lblNome = new JLabel("Nome Completo:*");
        lblNome.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblNome, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        txtNome = new JTextField(30);
        txtNome.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtNome, gbc);

        // Login
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblLogin = new JLabel("Login:*");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblLogin, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        txtLogin = new JTextField(30);
        txtLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtLogin, gbc);

        // Senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        String labelSenha = usuarioEdicao == null ? "Senha:*" : "Senha: (deixe em branco para não alterar)";
        JLabel lblSenha = new JLabel(labelSenha);
        lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblSenha, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        txtSenha = new JPasswordField(30);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtSenha, gbc);

        // Confirmar Senha
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel lblConfirmaSenha = new JLabel("Confirmar Senha:*");
        lblConfirmaSenha.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblConfirmaSenha, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        txtConfirmaSenha = new JPasswordField(30);
        txtConfirmaSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtConfirmaSenha, gbc);

        // Tipo de Usuário
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel lblTipo = new JLabel("Tipo de Usuário:*");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblTipo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        cmbTipoUsuario = new JComboBox<>(new String[]{"FUNCIONARIO", "ADMIN"});
        cmbTipoUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(cmbTipoUsuario, gbc);

        // Ativo
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        chkAtivo = new JCheckBox("Usuário Ativo");
        chkAtivo.setFont(new Font("Arial", Font.BOLD, 14));
        chkAtivo.setBackground(Color.WHITE);
        chkAtivo.setSelected(true);
        formPanel.add(chkAtivo, gbc);

        // Legenda
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        JLabel lblObrigatorio = new JLabel("* Campos obrigatórios");
        lblObrigatorio.setFont(new Font("Arial", Font.ITALIC, 12));
        lblObrigatorio.setForeground(Color.RED);
        formPanel.add(lblObrigatorio, gbc);

        // Botões
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(46, 204, 113));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setPreferredSize(new Dimension(150, 40));
        btnSalvar.addActionListener(e -> salvarUsuario());
        formPanel.add(btnSalvar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
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
        if (usuarioEdicao != null) {
            txtNome.setText(usuarioEdicao.getNome());
            txtLogin.setText(usuarioEdicao.getLogin());
            cmbTipoUsuario.setSelectedItem(usuarioEdicao.getTipoUsuario().getValor());
            chkAtivo.setSelected(usuarioEdicao.isAtivo());
        }
    }

    private void salvarUsuario() {
        try {
            String nome = txtNome.getText().trim();
            String login = txtLogin.getText().trim();
            String senha = new String(txtSenha.getPassword());
            String confirmaSenha = new String(txtConfirmaSenha.getPassword());
            String tipoStr = (String) cmbTipoUsuario.getSelectedItem();
            boolean ativo = chkAtivo.isSelected();

            if (nome.isEmpty() || login.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nome e Login são obrigatórios",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (usuarioEdicao == null && senha.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Senha é obrigatória para novo cadastro",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!senha.isEmpty() && !senha.equals(confirmaSenha)) {
                JOptionPane.showMessageDialog(this,
                        "As senhas não conferem",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            TipoUsuario tipo = TipoUsuario.fromString(tipoStr);

            boolean sucesso;
            if (usuarioEdicao == null) {
                // Cadastro
                sucesso = usuarioController.cadastrarUsuario(nome, login, senha, tipo);
            } else {
                // Edição
                sucesso = usuarioController.atualizarUsuario(
                        usuarioEdicao.getId(), nome, login,
                        senha.isEmpty() ? null : senha, tipo, ativo);
            }

            if (sucesso) {
                String mensagem = usuarioEdicao == null ?
                        "Usuário cadastrado com sucesso!" :
                        "Usuário atualizado com sucesso!";

                JOptionPane.showMessageDialog(this,
                        mensagem,
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                limparCampos();
                dashboardView.refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar usuário",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar usuário: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtConfirmaSenha.setText("");
        cmbTipoUsuario.setSelectedIndex(0);
        chkAtivo.setSelected(true);
        txtNome.requestFocus();
        usuarioEdicao = null;
    }
}