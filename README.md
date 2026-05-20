# GDS Console Project - Shop Added

## Change Summary

- Added `Shop.java`.
- Changed main menu option 2 to `Shop`.
- Shop handles game list, game search, game detail, and add-to-cart.
- Cart checkout is separated from shop:
  - Shop: add game to cart.
  - Cart / Purchase: select a cart item, purchase, payment, then add to library.
- Existing `MemberGamer`, `Cart`, `Purchase`, `Payment`, and `Library` structures were kept with minimal change.

## Run

```bash
javac -encoding UTF-8 -d out src/gds/*.java
java -cp out gds.Main
```

## Login

- Member Gamer: `user / user`
- Developer: `dev / dev`
- Administrator: `admin / admin`
