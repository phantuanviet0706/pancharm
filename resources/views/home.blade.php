@extends('layouts.app')

@section('title', 'Trang chủ')

@section('content')
    @include('partials.banner')

    <section class="best-seller">
        <div class="container">
            <h2>Sản phẩm bán chạy</h2>
            <div class="product-grid">
                @foreach ($bestSellers as $product)
                    <div class="product-card">
                        <img src="{{ asset('storage/' . $product->image) }}" alt="{{ $product->name }}">
                        <h3>{{ $product->name }}</h3>
                        <p>{{ number_format($product->price, 0, ',', '.') }}₫</p>
                        <a href="{{ route('product.show', $product->id) }}">Xem chi tiết</a>
                    </div>
                @endforeach
            </div>
        </div>
    </section>

    <!-- Thêm các section khác như: Bộ sưu tập, Ưu đãi, Tin tức, v.v. -->
@endsection
