<template>
  <div class="row m-auto" style="max-width: 500px">
    <div class="col p-4 text-center">
      <h2 style="color: white">
        Account <span style="font-family: 'Arial Black', serif">Suspended</span>
      </h2>
      <br />
      <p style="color: white">
        You are no longer permitted to access the application. Please contact an
        administrator if you want to reactivate your account.
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "SuspendedUserView",
  mounted() {
    document.body.classList.add("suspended-user-view");
  },
  beforeUnmount() {
    document.body.classList.remove("suspended-user-view");
  },
};
</script>

<style scoped></style>
