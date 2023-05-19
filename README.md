# 🚀 k-commerce
- 실무에서 자주 쓰이거나, 평소에 관심 있던 기술들을 이커머스 도메인으로 학습 및 구현한 프로젝트입니다.
- 배포 주소 : http://43.200.236.147
- 깃헙 주소
  - MSA : https://github.com/k-commerce/msa
  - Backend : https://github.com/k-commerce/server
  - Frontend : https://github.com/k-commerce/client

<br>

## ⛏️ Skills
- Spring Cloud Netflix
  - Eureka Server
  - Eureka Client
- Spring Cloud Gateway
- Spring Cloud OpenFeign
- Spring Cloud Circuit Breaker
  - Resilience4j

<br>

## 🏗️ System Architecture
<div>
  <img src="https://github.com/k-commerce/msa/assets/96934857/932367e9-24c5-4274-83e8-e095b4740d78">
</div>

- Gateway에서 인증/인가 처리
- OpenFeign(FeignClient)을 이용한 서비스 간 통신
  - Order  <--- (Gateway) --->  Item

