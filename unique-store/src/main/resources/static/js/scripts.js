async function carregarProdutos() {
    try {
        // Faz a requisição para a API de produtos
        const response = await fetch('/api/produtos');
        if (!response.ok) {
            throw new Error('Erro ao carregar produtos');
        }

        const produtos = await response.json(); 
        const produtosContainer = document.getElementById('produtos');

        if (!produtosContainer) {
            console.error('Container de produtos não encontrado.');
            return;
        }

        // Limpa o conteúdo atual do container
        produtosContainer.innerHTML = '';

        // Itera sobre a lista de produtos e cria os cards
        produtos.forEach(produto => {
            const card = `
                <div class="col-md-4">
                    <div class="card mb-4">
                        ${produto.imagemUrl ? `<img src="${produto.imagemUrl}" class="card-img-top" alt="${produto.nome || 'Produto sem nome'}">` : ''}
                        <div class="card-body">
                            <h5 class="card-title">${produto.nome || 'Produto sem nome'}</h5>
                            <p class="card-text">${produto.descricao || 'Sem descrição'}</p>
                            <p class="card-text">R$ ${produto.preco ? produto.preco.toFixed(2) : '0.00'}</p>                           
                            <button class="btn btn-primary" onclick="adicionarAoCarrinho(${produto.id})">Adicionar ao Carrinho</button>
                        </div>
                    </div>
                </div>
            `;
            produtosContainer.innerHTML += card; // Adiciona o card ao container
        });
    } catch (error) {
        console.error('Erro ao carregar produtos:', error);
        alert('Erro ao carregar produtos. Tente novamente mais tarde.');
    }
}

async function adicionarAoCarrinho(produtoId) {
    try {
        const usuarioEmail = localStorage.getItem('usuarioEmail');
        if (!usuarioEmail) {
            alert('Você precisa estar logado para adicionar produtos ao carrinho.');
            window.location.href = '/login';
            return;
        }

        const response = await fetch(`/carrinho/adicionar/${produtoId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        const result = await response.json(); // Converte a resposta para JSON

        if (!response.ok || !result.success) { // Garante que a resposta foi bem-sucedida
            throw new Error(result.message || 'Erro ao adicionar produto ao carrinho.');
        }

        alert('Produto adicionado ao carrinho!');
        exibirCarrinho(); 
    } catch (error) {
        console.error('Erro ao adicionar produto ao carrinho:', error);
        alert(error.message || 'Erro ao adicionar produto ao carrinho.');
    }
}


async function exibirCarrinho() {
    try {
        const response = await fetch('/carrinho/api');
        if (!response.ok) {
            throw new Error('Erro ao carregar carrinho');
        }

        const carrinho = await response.json();
        const carrinhoContainer = document.getElementById('carrinho-container');

        if (!carrinhoContainer) {
            console.error('Container do carrinho não encontrado.');
            return;
        }

        carrinhoContainer.innerHTML = '';

        if (carrinho.length > 0) {
            carrinho.forEach(produto => {
                const item = `
                    <div class="card mb-3">
                        <div class="row g-0">
                            <div class="col-md-4">
                                <img src="${produto.imagemUrl}" class="img-fluid rounded-start" alt="${produto.nome || 'Produto sem nome'}">
                            </div>
                            <div class="col-md-8">
                                <div class="card-body">
                                    <h5 class="card-title">${produto.nome || 'Produto sem nome'}</h5>
                                    <p class="card-text">${produto.descricao || 'Sem descrição'}</p>
                                    <p class="card-text">R$ ${produto.preco ? produto.preco.toFixed(2) : '0.00'}</p>
                                    <button class="btn btn-danger" onclick="removerDoCarrinho(${produto.id})">Remover</button>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                carrinhoContainer.innerHTML += item;
            });

            // Exibe o total do carrinho
            const total = carrinho.reduce((sum, produto) => sum + (produto.preco || 0), 0);
            document.getElementById('total').innerText = `Total: R$ ${total.toFixed(2)}`;
        } else {
            carrinhoContainer.innerHTML = '<p>O carrinho está vazio.</p>';
            document.getElementById('total').innerText = 'Total: R$ 0.00'; // Define o total como 0 se o carrinho estiver vazio
        }
    } catch (error) {
        console.error('Erro ao carregar carrinho:', error);
        alert('Erro ao carregar carrinho. Tente novamente mais tarde.');
    }
}

async function removerDoCarrinho(produtoId) {
    try {
        const response = await fetch(`/carrinho/remover/${produtoId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Erro ao remover produto do carrinho');
        }

        alert('Produto removido do carrinho!');
        exibirCarrinho();
    } catch (error) {
        console.error('Erro ao remover produto do carrinho:', error);
        alert('Erro ao remover produto do carrinho. Tente novamente.');
    }
}

// Função para fazer logout
function fazerLogout() {
    localStorage.removeItem('usuarioNome'); // Remove o nome do localStorage
    localStorage.removeItem('usuarioEmail'); // Remove o e-mail do localStorage
    window.location.href = '/logout'; // Redireciona para o endpoint de logout
}

document.addEventListener('DOMContentLoaded', function () {
    // Validação do formulário de registro
    const formRegistro = document.getElementById('formRegistro');
    if (formRegistro) {
        formRegistro.addEventListener('submit', function (event) {
            const senhaInput = document.getElementById('senha');
            const confirmarSenhaInput = document.getElementById('confirmarSenha');

            if (!senhaInput || !confirmarSenhaInput) return;

            if (senhaInput.value !== confirmarSenhaInput.value) {
                event.preventDefault();
                alert('As senhas não coincidem.');
            }
        });
    }

    // Verifica se há informações de usuário na sessão e atualiza o localStorage
    const usuarioNomeElement = document.getElementById('usuarioNome');
    const usuarioEmailElement = document.getElementById('usuarioEmail');

    if (usuarioNomeElement && usuarioEmailElement) {
        const usuarioNome = usuarioNomeElement.textContent;
        const usuarioEmail = usuarioEmailElement.textContent;

    if (usuarioNome && usuarioEmail) {
        localStorage.setItem('usuarioNome', usuarioNome);
        localStorage.setItem('usuarioEmail', usuarioEmail);
    }
    }

    var mensagem = document.getElementById("mensagem-sucesso");
    if (mensagem) {
        setTimeout(function() {
            mensagem.style.transition = "opacity 0.5s";
            mensagem.style.opacity = "0";
            setTimeout(function() {
                mensagem.remove(); // Remove a mensagem do DOM após a transição
            }, 500); // Tempo da transição (0.5s)
        }, 3000); // A mensagem desaparece após 3 segundos
    }
    
    // Validação do formulário de login
    const formLogin = document.getElementById('formLogin');
    if (formLogin) {
        formLogin.addEventListener('submit', function (event) {
            const emailInput = document.getElementById('email');
            const senhaInput = document.getElementById('senha');

            if (!emailInput || !senhaInput) return;

            if (!emailInput.value || !senhaInput.value) {
                event.preventDefault();
                alert('Preencha todos os campos.');
            }
        });
    }

    // Carregar produtos, carrinho e favoritos
    if (document.getElementById('produtos')) {
        carregarProdutos();
    }

    if (document.getElementById('carrinho-container')) {
        exibirCarrinho();
    }

    if (document.getElementById('favoritos-container')) {
        exibirFavoritos();
    }
});
