package controller;

import model.Produto;
import model.ProdutoDAO;
import model.Categoria;
import model.CategoriaDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@WebServlet("/produtos")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,       
        maxFileSize = 5 * 1024 * 1024,         
        maxRequestSize = 10 * 1024 * 1024     
)
public class ProdutoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if(req.getSession().getAttribute("usuarioLogado") == null){
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        ProdutoDAO dao = new ProdutoDAO();
        CategoriaDAO catDao = new CategoriaDAO();

        try {
            if(action == null || action.equals("listar")){
                List<Produto> produtos = dao.listar();
                req.setAttribute("produtos", produtos);

                String msg = (String) req.getSession().getAttribute("msg");
                if(msg != null){
                    req.setAttribute("msg", msg);
                    req.getSession().removeAttribute("msg");
                }

                req.getRequestDispatcher("/produtos/list.jsp").forward(req, resp);

            } else if(action.equals("editar")){
                String idStr = req.getParameter("id");
                Produto p = null;
                if(idStr != null && !idStr.isEmpty()){
                    p = dao.getProdutoById(Integer.parseInt(idStr));
                }
                List<Categoria> categorias = catDao.listar();
                req.setAttribute("produto", p);
                req.setAttribute("categorias", categorias);
                req.getRequestDispatcher("/produtos/form.jsp").forward(req, resp);

            } else if(action.equals("deletar")){
                int id = Integer.parseInt(req.getParameter("id"));
                Produto p = dao.getProdutoById(id);
                if(p != null && p.getImagem() != null){
                    String uploadPath = getServletContext().getRealPath("/uploads/produtos");
                    File file = new File(uploadPath, new File(p.getImagem()).getName());
                    if(file.exists()) file.delete();
                }
                dao.deletar(id);
                req.getSession().setAttribute("msg", "Produto excluído com sucesso!");
                resp.sendRedirect("produtos?action=listar");
            }

        } catch (Exception e){
            e.printStackTrace();
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("produtos?action=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        if(req.getSession().getAttribute("usuarioLogado") == null){
            resp.sendRedirect("login.jsp");
            return;
        }

        Produto p = new Produto();
        ProdutoDAO dao = new ProdutoDAO();

        // Dados do formulário
        String idParam = req.getParameter("id");
        p.setId(idParam != null && !idParam.trim().isEmpty() ?
                Integer.parseInt(idParam.trim()) : 0);

        p.setNome(req.getParameter("nome") != null ? req.getParameter("nome").trim() : "");
        p.setPreco(req.getParameter("preco") != null && !req.getParameter("preco").trim().isEmpty() ?
                Double.parseDouble(req.getParameter("preco").trim()) : 0.0);
        p.setEstoque(req.getParameter("estoque") != null && !req.getParameter("estoque").trim().isEmpty() ?
                Integer.parseInt(req.getParameter("estoque").trim()) : 0);
        p.setCategoriaId(req.getParameter("categoria_id") != null && !req.getParameter("categoria_id").trim().isEmpty() ?
                Integer.parseInt(req.getParameter("categoria_id").trim()) : 0);

        try {
            // Upload de imagem
            Part filePart = req.getPart("imagem");
            if(filePart != null && filePart.getSize() > 0){
                String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                String uploadPath = getServletContext().getRealPath("/uploads/produtos");
                File uploadDir = new File(uploadPath);
                if(!uploadDir.exists()) uploadDir.mkdirs();

                File file = new File(uploadDir, fileName);
                try(InputStream input = filePart.getInputStream()){
                    Files.copy(input, file.toPath());
                }
                p.setImagem("uploads/produtos/" + fileName); // salva caminho relativo
            } else if(p.getId() != 0){
                Produto antigo = dao.getProdutoById(p.getId());
                if(antigo != null) p.setImagem(antigo.getImagem());
            }

            if(p.getId() == 0){
                dao.salvar(p);
                req.getSession().setAttribute("msg", "Produto cadastrado com sucesso!");
            } else {
                dao.atualizar(p);
                req.getSession().setAttribute("msg", "Produto atualizado com sucesso!");
            }

            resp.sendRedirect("produtos?action=listar");

        } catch (Exception e){
            e.printStackTrace();
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("produtos?action=listar");
        }
    }
}
