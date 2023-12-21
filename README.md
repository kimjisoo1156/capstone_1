# 23-2학기 capstone 1조
# 주제 : VR, 인공지능을 활용한 던전 RPG 제작 및 웹 서비스 제공


## 🌏Darkest Planet
##### <http://d10v0gqnjifjsl.cloudfront.net> <- 웹사이트 주소
##### https://www.youtube.com/watch?v=yHFZAzzTs5Q&t=123s <- 시연영상 (VR rpg게임 및 웹서비스)
##### 해당 프로젝트는 국가 R&D에 프로그램 등록까지 되었습니다. 등록번호 C-2023-060485

## 유니티 주요 기능
- 적 인공지능(회피,탐지,도망,추적)
- 회피 : 적에게 설정된 회피 범위 플레이어의 무기가 탐지될 경우 회피 동작을 수행
- 탐지 : 탐지 범위내 플레이어가 들어오는지 안 들어 오는지 확인(상시 동작)
- 도망 : 적 HP가 30% 이하로 떨어질 경우 랜덤 회복 포인트 중 가장 가까운 곳으로 도망가는 동작 수행
- 추적 : 탐지 범위내 플레이어가 들어오게 된다면 플레이어를 추적함(기본 동작)
- 강화 시스템 (강화에 따라서 강해짐): 강화 NPC로부터 몸을 강화 함으로써 적을 좀더 쉽게 처리 할 수 있게 됨.
- 세이브 기능 (진행 상황 기록 가능) : 저장 기능을 통해 진행 상황을 저장하고 차후 불러와서 플 레이 할 수 있음.
- 스토리 기능 (진행 상황에 따라 이야기 추적): 진행 상황에 따라 게임 속 정해진 스토리를 볼 수 있음.
- Low-poly를 통한 최적화 기능 : Low-poly 그래픽을 차용함으로써 저사양 컴퓨터(오큘러스 퀘스트 2 권장 사양보다 낮은 컴퓨터)로도 플레이 가능
- 사운드 기능 (상황에 알맞은 적절한 사운드 출력) : 사운드를 탑재하여 적절한 상황에 사운드 효 과 부여

## 웹서비스 주요 기능
- 회원가입
- 로그인, 로그아웃
- JWT토큰 기반 로그인, 로그아웃
- 게시물 생성, 조회, 수정, 삭제 기능
- 이미지 첨부 기능 (업로드, 수정, 삭제)
- 검색 기능 (제목, 내용, 작성자, 제목 + 내용)
- 댓글 기능 (등록, 수정, 삭제)
- 비밀번호 찾기 기능
- 다운로드 기능
- 내 정보 수정
- 회원 탈퇴 기능

#### 메인 화면
<img width="905" src="[https://github.com/kimjisoo1156/capstone_1/assets/121778107/8718db2e-ce3e-4bcc-9b53-ca8a6bec83e2](https://github.com/kimjisoo1156/capstone_1/assets/121778107/04e9a05f-68a0-4ae3-917c-7e6b6f472b94)">

###### 게시판 이미지 업로드시 화면
<img width="701" alt="KakaoTalk_Photo_2023-11-03-13-46-31" src="https://github.com/kimjisoo1156/capstone_1/assets/121778107/cc5f8aaa-ec3f-4a5f-8322-e03379d7a83c">

###### 검색 시
![KakaoTalk_Photo_2023-11-03-13-47-14](https://github.com/kimjisoo1156/capstone_1/assets/121778107/6f86fd31-57c8-4d56-9611-861e61bd9f2a)
![검색 기능](https://github.com/VaIice/Capstone/assets/141003473/1c873342-e3ca-4b9d-9b35-5f9340cacca7)

###### 게시글 삭제 시
![KakaoTalk_Photo_2023-11-03-13-47-38](https://github.com/kimjisoo1156/capstone_1/assets/121778107/21ca0ec5-3eaf-440f-8b13-4ab01dca3f6c)

###### 웹시연_1
https://github.com/kimjisoo1156/capstone_1/assets/121778107/0adb7750-a125-4883-a891-07824174c5dc

---
## 👥팀 소개
#### 🎮 유니티
| :------------: |
| 팀장 : 문경필|
|  ****1548 |

#### 🎮 유니티
| :------------: |
|  박승완|
|  ****1552 |

#### 🖥BackEnd
|   BackEnd |
| :------------: |
| 김지수|
|  ****1570 |
| [![GitHub](https://img.shields.io/badge/-GitHub-black?style=flat-square&logo=github)](https://github.com/kimjisoo1156) |

🛠️ **Tools and Technologies :** <br><br>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=mysql&logoColor=white"> 
<img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white"> 
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=aws&logoColor=white">
<img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
<img src="https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white">
<img src="https://img.shields.io/badge/githubactions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white">
<img src="https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">

#### 🎨FrontEnd
|   FrontEnd |
| :------------: |
| 김승현|
|  ****1677 |
| [![GitHub](https://img.shields.io/badge/-GitHub-black?style=flat-square&logo=github)](https://github.com/VaIice) |

🛠️ **Tools and Technologies :** <br><br>
![HTML5](https://img.shields.io/badge/HTML5-%23E34F26.svg?&style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/-CSS3-1572B6?logo=css3&logoColor=white&style=for-the-badge)
![JavaScript](https://img.shields.io/badge/JavaScript-%23F7DF1E.svg?&style=for-the-badge&logo=javascript&logoColor=black)
![React](https://img.shields.io/badge/React-%2361DAFB.svg?&style=for-the-badge&logo=react&logoColor=white)
![Amazon S3](https://img.shields.io/badge/Amazon_S3-%23D9313D.svg?&style=for-the-badge&logo=amazon-aws&logoColor=white)
![Amazon CloudFront](https://img.shields.io/badge/Amazon_CloudFront-%23FF9900.svg?&style=for-the-badge&logo=amazon-aws&logoColor=black)
![Figma](https://img.shields.io/badge/-Figma-F24E1E?logo=Figma&logoColor=white&style=for-the-badge)
