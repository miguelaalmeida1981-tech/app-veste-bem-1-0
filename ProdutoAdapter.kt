package com.vestbem.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vestbem.app.data.Produto
import com.vestbem.app.databinding.ItemProdutoBinding

class ProdutoAdapter(
    private val onClick: (Produto) -> Unit
) : ListAdapter<Produto, ProdutoAdapter.ProdutoViewHolder>(ProdutoDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val binding = ItemProdutoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProdutoViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ProdutoViewHolder(
        private val binding: ItemProdutoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(produto: Produto) {
            with(binding) {
                tvNome.text = produto.nome
                tvPreco.text = "R$ ${String.format("%.2f", produto.preco)}"
                tvAvaliacao.text = "★ ${produto.avaliacao} (${produto.numAvaliacoes})"
                
                Glide.with(root.context)
                    .load(produto.imagemUrl)
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(ivProduto)
                
                root.setOnClickListener { onClick(produto) }
            }
        }
    }
}

class ProdutoDiffCallback : DiffUtil.ItemCallback<Produto>() {
    override fun areItemsTheSame(oldItem: Produto, newItem: Produto): Boolean {
        return oldItem.id == newItem.id
    }
    
    override fun areContentsTheSame(oldItem: Produto, newItem: Produto): Boolean {
        return oldItem == newItem
    }
}
