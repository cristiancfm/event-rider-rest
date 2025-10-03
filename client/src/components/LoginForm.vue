<template>
  <h1 class="mt-3">
    Log <span style="font-family: 'Arial Black', serif">In</span>
  </h1>
  <form
    class="m-auto mt-2 p-3"
    style="max-width: 400px"
    @submit.prevent="login()"
  >
    <div class="mb-2 text-start">
      <label for="email" class="form-label">Email Address</label>
      <input
        type="text"
        class="form-control"
        id="email"
        v-model="loginForm.email"
        required
        minlength="4"
      />
    </div>
    <div class="mb-2 text-start">
      <label for="password" class="form-label">Password</label>
      <input
        type="password"
        class="form-control"
        id="password"
        v-model="loginForm.password"
        required
        minlength="4"
      />
    </div>
    <button
      type="submit"
      class="btn btn-primary mt-2"
      @click="loginForm.error = null"
    >
      Submit
    </button>
    <div
      class="alert alert-danger alert-dismissible fade show mt-3"
      role="alert"
      v-if="loginForm.error"
    >
      <strong>Error:</strong> {{ loginForm.error }}
      <button
        type="button"
        class="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
        @click="loginForm.error = null"
      ></button>
    </div>
  </form>
</template>

<script>
import auth from "@/common/auth";

export default {
  data() {
    return {
      loginForm: {
        email: null,
        password: null,
        error: null,
      },
    };
  },
  methods: {
    async login() {
      try {
        await auth.login(this.loginForm);
        this.$router.go(-1);
      } catch (err) {
        const response = JSON.parse(err.request.response);
        this.loginForm.error = response.message;
        console.error(err);
      }
    },
  },
};
</script>
