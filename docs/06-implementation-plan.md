# 러닝 트래커 앱 구현 단계별 프로젝트 설정 (참고용)

이 문서는 러닝 트래커 구현을 위한 초기 설정 및 개발 로드맵을 정리한 자료입니다.

## 1. 매니페스트 설정 (권한 및 서비스 선언)
- **위치 권한**: `FINE_LOCATION`, `COARSE_LOCATION`, `BACKGROUND_LOCATION` (Android 10+ 대응)
- **포그라운드 서비스**: `FOREGROUND_SERVICE_LOCATION` 타입 선언 필수 (Android 14+ 대응)
- **서비스 등록**: `LocationService` (실제 위치 수집 로직 담당)

## 2. 의존성 관리 (중요 라이브러리)
- **Room DB**: 운동 기록 로컬 저장용
- **Dagger-Hilt**: 의존성 주입(DI)을 통한 모듈 간 결합도 완화
- **Google Maps SDK**: 실시간 경로 표시 및 지도 UI
- **Play Services Location**: 위치 정보 수집 핵심 라이브러리

## 3. 데이터 레이어 구성 요소
- **RunEntity**: Room DB용 테이블 모델
- **RunningDao**: DB 접근을 위한 Data Access Object
- **RunningRepository**: Domain 레이어와 통신하기 위한 추상화 계층

## 4. 검증 계획
- 의존성 추가 후 정상 빌드 여부 확인 (`assembleDebug`)
- 앱 실행 시 위치 권한 요청 프로세스 동작 여부 확인
