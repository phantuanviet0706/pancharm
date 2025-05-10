<header>
    <div class="container">
        <div class="logo">
            <a href="{{ route('home') }}">
                <img src="{{ asset('images/logo.png') }}" alt="Lavesta">
            </a>
        </div>
        <nav>
            <ul>
                <li><a href="{{ route('home') }}">Trang chủ</a></li>
                <li><a href="{{ route('about') }}">Giới thiệu</a></li>
                <li><a href="{{ route('contact') }}">Liên hệ</a></li>
            </ul>
        </nav>
    </div>
</header>
