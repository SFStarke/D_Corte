-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 20-Abr-2023 às 21:07
-- Versão do servidor: 10.4.19-MariaDB
-- versão do PHP: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `salon`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `agendas`
--

CREATE TABLE `agendas` (
  `id` int(11) NOT NULL,
  `cabeleireiro` varchar(40) DEFAULT NULL,
  `manicure` varchar(40) DEFAULT NULL,
  `cliente` varchar(40) DEFAULT NULL,
  `servico` text DEFAULT NULL,
  `valor_total` varchar(15) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `horario` varchar(5) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `agendas`
--

INSERT INTO `agendas` (`id`, `cabeleireiro`, `manicure`, `cliente`, `servico`, `valor_total`, `data`, `horario`) VALUES
(2, 'Marga Lickfield', 'Sophia Amoruso', 'Debora Debronet', 'Corte Feminino. Manicure & Pedicure. ', '135.0', '2023-04-24', '14:00'),
(3, 'Sérgio Felipe Starke', '', 'Sylar Hero', 'Corte Masculino. Barba. ', '85.0', '2023-04-22', '09:00'),
(4, 'Christopher McCanless', '', 'Beatriz Cristina Bublitz', 'Corte Feminino. Tingimento. ', '355.0', '2023-04-20', '11:00'),
(5, 'Sérgio Felipe Starke', '', 'Marcelo Youth', 'Corte Masculino. ', '45.0', '2020-04-26', '16:40'),
(6, 'Marga Lickfield', '', 'Francisco Fraterno', 'Corte Masculino. ', '45.0', '2020-04-15', '07:47'),
(7, 'Christopher McCanless', '', 'Gilberto Father', 'Barba. ', '40.0', '2023-04-20', '12:30'),
(8, 'Sérgio Felipe Starke', 'Flavia Fatchinni', 'Sieglinde Grasmann', 'Corte Feminino. Tingimento. Manicure & Pedicure. Penteado. ', '515.0', '2022-11-20', '06:47'),
(9, 'Christopher McCanless', '', 'Sylar Hero', 'Corte Masculino. ', '45.0', '2023-02-20', '07:47'),
(10, '', '', 'null', 'Corte Masculino. Barba. ', '85.0', '2023-04-20', '13:40'),
(11, 'Marga Lickfield', '', 'Emanuel Sérgio Girardi', 'Corte Masculino. ', '45.0', '2021-06-10', '07:47'),
(12, '', 'Sophia Amoruso', 'Beatriz Cristina Bublitz', 'Manicure & Pedicure. ', '80.0', '2023-04-20', '07:47'),
(13, '', 'Sophia Amoruso', 'Maurisa Sanders', 'Manicure & Pedicure. ', '80.0', '2021-11-17', '17:47'),
(14, '', 'Flavia Fatchinni', 'Ana Amorin', 'Manicure & Pedicure. Pedicure. ', '120.0', '2023-04-20', '07:47'),
(15, '', 'Flavia Fatchinni', 'Maurisa Sanders', 'Manicure & Pedicure. ', '80.0', '2021-11-17', '17:47'),
(16, 'Sérgio Felipe Starke', '', 'Jaqueline Jacks', 'Corte Feminino. ', '55.0', '2023-04-20', '13:20'),
(17, 'Marga Lickfield', '', 'Debora Debronet', 'Tingimento. ', '300.0', '2023-04-20', '10:00');

-- --------------------------------------------------------

--
-- Estrutura da tabela `clientes`
--

CREATE TABLE `clientes` (
  `id` int(11) NOT NULL,
  `nome` varchar(40) NOT NULL,
  `telefone` varchar(40) NOT NULL,
  `complemento` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `clientes`
--

INSERT INTO `clientes` (`id`, `nome`, `telefone`, `complemento`) VALUES
(1, 'Amanda Reis', '47-10000.00.01', 'Cliente preferencial...'),
(2, 'Marcelo Youth', '47-00000.00.02', 'Cliente02'),
(3, 'Beatriz Cristina Bublitz', '47-00000.00.03', 'Cliente03'),
(4, 'Gilberto Father', '47-00000.00.04', 'Cliente04'),
(5, 'Sieglinde Grasmann', '47-00000.00.05', 'Cliente05 Minha vovó'),
(6, 'Sylar Hero', '47-00000.00.06', 'Cliente06\n'),
(7, 'Maurisa Sanders', '47-00000.00.07', 'Corte curto esquadrado três desdos.'),
(8, 'Eliandro Elgin', '47-00000.00.08', 'Cliente08'),
(9, 'Francisco Fraterno', '47-00000.00.09', 'Um dedo e meio degradê social.'),
(10, 'Ana Amorin', '47-00000.00.10', 'Cliente10'),
(12, 'Emanuel Sérgio Girardi', '47-00000.00.11', '   Mano guerreiro...'),
(13, 'Gustavo Gusmão', '00000.00.13', ''),
(14, 'Adriana Adrim', '47-00000.00.14', ''),
(15, 'Debora Debronet', '47-00000.00.15', '   Prefere com a Marga\n   Aniver dia 27 de Agosto\ndebronet@gmail.com'),
(16, 'null', 'null', ''),
(17, 'Athur Adrim', '47-00000.00.17', 'aa@outlook.com'),
(18, 'Jaqueline Jacks', '47-00000.00.18', 'Prefere sempre com o Sérgio e a Sophia');

-- --------------------------------------------------------

--
-- Estrutura da tabela `funcionarios`
--

CREATE TABLE `funcionarios` (
  `id` int(11) NOT NULL,
  `nome` varchar(40) NOT NULL,
  `cpf` varchar(20) DEFAULT NULL,
  `cnpj` varchar(20) DEFAULT NULL,
  `telefone` varchar(40) NOT NULL,
  `ocupacao` varchar(20) NOT NULL,
  `complemento` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `funcionarios`
--

INSERT INTO `funcionarios` (`id`, `nome`, `cpf`, `cnpj`, `telefone`, `ocupacao`, `complemento`) VALUES
(1, 'Christopher McCanless', '000.000.000-01', '00.000.000./0000-01', '47-00000.00.01', 'Cabeleireiro(a)', '   Entusiasta por uma vida livre de futilidades...'),
(2, 'Sophia Amoruso', '000.000.000-02', '00.000.000./0000-02', '47-00000.00.02', 'Manicure', '   Testemunho de que, quando encontramos\num trablho pelo que amamos, todo esforço vale\na pena'),
(3, 'Marga Lickfield', '000.000.000-03', '00.000.000./0000-03', '47-00000.00.03', 'Cabeleireiro(a)', '   Exemplo de perseverença em Deus,\nmesmo em meio a dor.'),
(4, 'Flavia Fatchinni', '000.000.000-04', 'sem', '47-00000.00.04', 'Manicure', ''),
(5, 'Sérgio Felipe Starke', '000.000.000-05', '00.000.000./0000-05', '47-00000.00.05', 'Cabeleireiro(a)', '');

-- --------------------------------------------------------

--
-- Estrutura da tabela `seguranca`
--

CREATE TABLE `seguranca` (
  `id` int(11) NOT NULL,
  `usuario` varchar(20) NOT NULL,
  `login` varchar(15) NOT NULL,
  `senha` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Extraindo dados da tabela `seguranca`
--

INSERT INTO `seguranca` (`id`, `usuario`, `login`, `senha`) VALUES
(1, 'Administrador', ' ', ' '),
(2, 'User', 'user', '1234');

-- --------------------------------------------------------

--
-- Estrutura da tabela `servicos`
--

CREATE TABLE `servicos` (
  `id` int(11) NOT NULL,
  `servico` varchar(50) NOT NULL,
  `valor` decimal(7,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `servicos`
--

INSERT INTO `servicos` (`id`, `servico`, `valor`) VALUES
(1, 'Corte Masculino', '45.00'),
(2, 'Corte Feminino', '55.00'),
(3, 'Tingimento', '300.00'),
(4, 'Alisamento', '80.00'),
(5, 'Penteado', '80.00'),
(6, 'Manicure', '40.00'),
(7, 'Pedicure', '40.00'),
(8, 'Manicure & Pedicure', '80.00'),
(9, 'Barba', '40.00'),
(10, 'Lavar Cabelo', '20.00');

--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `agendas`
--
ALTER TABLE `agendas`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `funcionarios`
--
ALTER TABLE `funcionarios`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `seguranca`
--
ALTER TABLE `seguranca`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

--
-- Índices para tabela `servicos`
--
ALTER TABLE `servicos`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `agendas`
--
ALTER TABLE `agendas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de tabela `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT de tabela `funcionarios`
--
ALTER TABLE `funcionarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de tabela `seguranca`
--
ALTER TABLE `seguranca`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de tabela `servicos`
--
ALTER TABLE `servicos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
